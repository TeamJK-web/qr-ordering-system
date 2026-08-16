package com.restaurant.ordering.controller;
import com.restaurant.ordering.dto.*;
import com.restaurant.ordering.model.*;
import com.restaurant.ordering.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;
@RestController @RequestMapping("/api/restaurants/{restaurantSlug}/orders") @CrossOrigin
public class OrderController { private final OrderService service; public OrderController(OrderService service){this.service=service;} @PostMapping public Order placeOrder(@PathVariable String restaurantSlug,@RequestBody PlaceOrderRequest request){return service.placeOrder(restaurantSlug,request);} @GetMapping public List<Order> getKitchenOrders(@PathVariable String restaurantSlug){return service.getKitchenOrders(restaurantSlug);} @GetMapping("/stream") public SseEmitter stream(){return service.subscribe();} @GetMapping("/{id}") public Order order(@PathVariable String restaurantSlug,@PathVariable Long id){return service.getOrder(restaurantSlug,id);}@PatchMapping("/{id}/done") public void markDone(@PathVariable String restaurantSlug,@PathVariable Long id,@RequestParam(required=false) Long employeeId){service.markDone(restaurantSlug,id,employeeId);} @PostMapping("/{id}/payment") public Payment pay(@PathVariable String restaurantSlug,@PathVariable Long id,@RequestBody PaymentRequest request){return service.pay(restaurantSlug,id,request);} }
