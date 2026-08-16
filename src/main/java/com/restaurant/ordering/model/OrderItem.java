package com.restaurant.ordering.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonBackReference @ManyToOne(optional=false) @JoinColumn(name="order_id") private Order order;
    @Column(nullable=false) private String name;
    private String emoji;
    @Column(nullable=false) private int quantity;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal price;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal subtotal;
    protected OrderItem() {}
    public OrderItem(Order order, MenuItem menuItem, int quantity) { this.order=order; name=menuItem.getName(); emoji=menuItem.getEmoji(); this.quantity=quantity; price=menuItem.getPrice(); subtotal=price.multiply(BigDecimal.valueOf(quantity)); }
    public String getName(){return name;} public String getEmoji(){return emoji;} public int getQuantity(){return quantity;} public BigDecimal getPrice(){return price;} public BigDecimal getSubtotal(){return subtotal;}
}
