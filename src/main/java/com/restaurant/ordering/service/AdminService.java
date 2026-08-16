package com.restaurant.ordering.service;
import com.restaurant.ordering.dto.*;
import com.restaurant.ordering.model.*;
import com.restaurant.ordering.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.*;

@Service public class AdminService {
 private final RestaurantRepository restaurants; private final MenuItemRepository menuItems; private final EmployeeRepository employees; private final OrderRepository orders; private final PasswordEncoder passwordEncoder;
 public AdminService(RestaurantRepository restaurants,MenuItemRepository menuItems,EmployeeRepository employees,OrderRepository orders,PasswordEncoder passwordEncoder){this.restaurants=restaurants;this.menuItems=menuItems;this.employees=employees;this.orders=orders;this.passwordEncoder=passwordEncoder;}
 public List<MenuItem> menu(String slug){restaurant(slug);return menuItems.findByRestaurantSlugOrderByCategoryAscNameAsc(slug);}
 public List<Employee> employees(String slug){restaurant(slug);return employees.findByRestaurantSlugOrderByNameAsc(slug);}
 public List<Order> orders(String slug){restaurant(slug);return orders.findAllForRestaurant(slug);}
 @Transactional public MenuItem createMenuItem(String slug,MenuItemRequest r){Restaurant restaurant=restaurant(slug);validateMenu(r);return menuItems.save(new MenuItem(restaurant,r.name().trim(),nullable(r.description()),r.price(),r.category().trim(),nullable(r.emoji())));}
 @Transactional public MenuItem updateMenuItem(String slug,Long id,MenuItemRequest r){validateMenu(r);MenuItem item=menuItems.findByIdAndRestaurantSlug(id,slug).orElseThrow(()->notFound("Menu item"));item.update(r.name().trim(),nullable(r.description()),r.price(),r.category().trim(),nullable(r.emoji()),r.available());return item;}
 @Transactional public Employee createEmployee(String slug,EmployeeRequest r){Restaurant restaurant=restaurant(slug);validateEmployee(r);if(r.pin()==null||r.pin().isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A PIN is required for a new employee");return employees.save(new Employee(restaurant,r.name().trim(),r.role(),hash(r.pin())));}
 @Transactional public Employee updateEmployee(String slug,Long id,EmployeeRequest r){validateEmployee(r);Employee employee=employees.findByIdAndRestaurantSlug(id,slug).orElseThrow(()->notFound("Employee"));employee.update(r.name().trim(),r.role(),r.pin()==null||r.pin().isBlank()?null:hash(r.pin()));return employee;}
 public DashboardSummary summary(String slug){List<Order> all=orders(slug);long pending=all.stream().filter(o->o.getStatus()==Order.Status.PENDING||o.getStatus()==Order.Status.PREPARING).count();long ready=all.stream().filter(o->o.getStatus()==Order.Status.READY).count();List<Order> paid=all.stream().filter(o->o.getStatus()==Order.Status.PAID).toList();BigDecimal sales=paid.stream().map(Order::getTotal).reduce(BigDecimal.ZERO,BigDecimal::add);return new DashboardSummary(pending,ready,paid.size(),sales);}
 private Restaurant restaurant(String slug){return restaurants.findBySlug(slug).orElseThrow(()->notFound("Restaurant"));} private void validateMenu(MenuItemRequest r){if(r.name()==null||r.name().isBlank()||r.category()==null||r.category().isBlank()||r.price()==null||r.price().signum()<0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name, category, and a non-negative price are required");} private void validateEmployee(EmployeeRequest r){if(r.name()==null||r.name().isBlank()||r.role()==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name and role are required");} private String hash(String pin){return pin==null||pin.isBlank()?null:passwordEncoder.encode(pin);} private ResponseStatusException notFound(String type){return new ResponseStatusException(HttpStatus.NOT_FOUND,type+" not found");} private String nullable(String value){return value==null?null:value.trim();}
}
