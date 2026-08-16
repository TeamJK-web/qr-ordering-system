package com.restaurant.ordering.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "restaurant_id") private Restaurant restaurant;
    @Column(nullable = false, length = 20) private String tableNumber;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.PENDING;
    @Column(nullable = false) private LocalDateTime placedAt = LocalDateTime.now();
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal subtotal;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal serviceCharge;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal total;
    @ManyToOne @JoinColumn(name = "prepared_by_employee_id") private Employee preparedBy;
    @JsonManagedReference @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true) private List<OrderItem> items = new ArrayList<>();
    public enum Status { PENDING, PREPARING, READY, PAID, CANCELLED }
    protected Order() {}
    public Order(Restaurant restaurant, String tableNumber, BigDecimal subtotal, BigDecimal serviceCharge) { this.restaurant=restaurant; this.tableNumber=tableNumber; this.subtotal=subtotal; this.serviceCharge=serviceCharge; this.total=subtotal.add(serviceCharge); }
    public void addItem(OrderItem item){items.add(item);} public void markReady(Employee employee){status=Status.READY; preparedBy=employee;} public void markPaid(){status=Status.PAID;}
    public Long getId(){return id;} public String getTableNumber(){return tableNumber;} public Status getStatus(){return status;} public LocalDateTime getPlacedAt(){return placedAt;} public BigDecimal getSubtotal(){return subtotal;} public BigDecimal getServiceCharge(){return serviceCharge;} public BigDecimal getTotal(){return total;} public List<OrderItem> getItems(){return items;} public Employee getPreparedBy(){return preparedBy;}
}
