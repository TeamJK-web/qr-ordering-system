package com.restaurant.ordering.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
    private String tableNumber;
    private List<OrderItem> items;
    private String status;
    private String placedAt;
    private double subtotal;
    private double serviceCharge;
    private double total;

    public Order() {}

    public Order(int id, String tableNumber, List<OrderItem> items) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.items = items;
        this.status = "PENDING";
        this.placedAt = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        this.subtotal = items.stream().mapToDouble(OrderItem::getSubtotal).sum();
        this.serviceCharge = this.subtotal * 0.10;
        this.total = this.subtotal + this.serviceCharge;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlacedAt() { return placedAt; }
    public void setPlacedAt(String placedAt) { this.placedAt = placedAt; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(double serviceCharge) { this.serviceCharge = serviceCharge; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
