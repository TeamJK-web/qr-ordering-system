package com.restaurant.ordering.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.ordering.model.Order;
import com.restaurant.ordering.model.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final List<Order> orders = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Order placeOrder(String tableNumber, List<OrderItem> items) {
        Order order = new Order(counter.incrementAndGet(), tableNumber, items);
        orders.add(order);
        broadcast("new-order", order);
        return order;
    }

    public List<Order> getPendingOrders() {
        return orders.stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .toList();
    }

    public void markDone(int id) {
        orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .ifPresent(o -> {
                    o.setStatus("DONE");
                    broadcast("order-done", id);
                });
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }

    private void broadcast(String eventName, Object data) {
        List<SseEmitter> dead = new CopyOnWriteArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                String json = objectMapper.writeValueAsString(data);
                emitter.send(SseEmitter.event().name(eventName).data(json));
            } catch (Exception e) {
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }
}
