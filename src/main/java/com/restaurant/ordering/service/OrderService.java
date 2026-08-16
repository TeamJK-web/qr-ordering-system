package com.restaurant.ordering.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.ordering.dto.*;
import com.restaurant.ordering.model.*;
import com.restaurant.ordering.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.math.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
@Service public class OrderService {
 private static final BigDecimal SERVICE_CHARGE_RATE=new BigDecimal("0.10"); private final RestaurantRepository restaurants; private final MenuItemRepository menuItems; private final OrderRepository orders; private final EmployeeRepository employees; private final PaymentRepository payments; private final List<SseEmitter> emitters=new CopyOnWriteArrayList<>(); private final ObjectMapper json=new ObjectMapper();
 public OrderService(RestaurantRepository restaurants,MenuItemRepository menuItems,OrderRepository orders,EmployeeRepository employees,PaymentRepository payments){this.restaurants=restaurants;this.menuItems=menuItems;this.orders=orders;this.employees=employees;this.payments=payments;}
 @Transactional public Order placeOrder(String slug,PlaceOrderRequest request){if(request.items()==null||request.items().isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"An order needs at least one item"); Restaurant restaurant=restaurant(slug); List<MenuItem> selected=new ArrayList<>(); BigDecimal subtotal=BigDecimal.ZERO; for(PlaceOrderRequest.Item r:request.items()){if(r.menuItemId()==null||r.quantity()<1)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid order item");MenuItem item=menuItems.findByIdAndRestaurantSlugAndAvailableTrue(r.menuItemId(),slug).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Menu item is unavailable"));selected.add(item);subtotal=subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(r.quantity())));} subtotal=subtotal.setScale(2,RoundingMode.HALF_UP);Order order=new Order(restaurant,request.tableNumber()==null||request.tableNumber().isBlank()?"—":request.tableNumber().trim(),subtotal,subtotal.multiply(SERVICE_CHARGE_RATE).setScale(2,RoundingMode.HALF_UP));for(int i=0;i<selected.size();i++)order.addItem(new OrderItem(order,selected.get(i),request.items().get(i).quantity()));Order saved=orders.save(order);broadcast("new-order",saved);return saved;}
 public List<Order> getKitchenOrders(String slug){restaurant(slug);return orders.findKitchenOrders(slug,List.of(Order.Status.PENDING,Order.Status.PREPARING));}
 @Transactional public void markDone(String slug,Long orderId,Long employeeId){Order order=orders.findByIdAndRestaurantSlug(orderId,slug).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));Employee employee=employeeId==null?employees.findFirstByRestaurantSlugAndRole(slug,Employee.Role.KITCHEN).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"No kitchen employee configured")):employees.findByIdAndRestaurantSlug(employeeId,slug).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee not found"));order.markReady(employee);broadcast("order-done",orderId);}
 @Transactional public Payment pay(String slug,Long orderId,PaymentRequest request){Order order=orders.findByIdAndRestaurantSlug(orderId,slug).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));Employee cashier=employees.findByIdAndRestaurantSlug(request.cashierId(),slug).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cashier not found"));if(cashier.getRole()!=Employee.Role.CASHIER)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee is not a cashier");Payment payment=payments.save(new Payment(order,cashier,request.method(),order.getTotal()));order.markPaid();broadcast("order-paid",orderId);return payment;}
 public SseEmitter subscribe(){SseEmitter e=new SseEmitter(Long.MAX_VALUE);emitters.add(e);e.onCompletion(()->emitters.remove(e));e.onTimeout(()->emitters.remove(e));e.onError(x->emitters.remove(e));return e;}
 private Restaurant restaurant(String slug){return restaurants.findBySlug(slug).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Restaurant not found"));}
 private void broadcast(String name,Object data){for(SseEmitter e:emitters)try{e.send(SseEmitter.event().name(name).data(json.writeValueAsString(data)));}catch(Exception ex){emitters.remove(e);}}
}
