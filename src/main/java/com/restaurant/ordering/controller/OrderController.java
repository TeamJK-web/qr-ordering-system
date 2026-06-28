package com.restaurant.ordering.controller;

import com.restaurant.ordering.model.Order;
import com.restaurant.ordering.model.OrderItem;
import com.restaurant.ordering.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(@RequestBody Map<String, Object> body) {
        String tableNumber = (String) body.getOrDefault("tableNumber", "—");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rawItems = (List<Map<String, Object>>) body.get("items");

        List<OrderItem> items = rawItems.stream().map(r -> new OrderItem(
                (String) r.get("name"),
                (String) r.get("emoji"),
                ((Number) r.get("quantity")).intValue(),
                ((Number) r.get("price")).doubleValue()
        )).toList();

        return orderService.placeOrder(tableNumber, items);
    }

    @GetMapping
    public List<Order> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        return orderService.subscribe();
    }

    @PatchMapping("/{id}/done")
    public void markDone(@PathVariable int id) {
        orderService.markDone(id);
    }
}
