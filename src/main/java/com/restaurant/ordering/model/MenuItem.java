package com.restaurant.ordering.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "menu_items")
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "restaurant_id") private Restaurant restaurant;
    @Column(nullable = false) private String name;
    private String description;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal price;
    @Column(nullable = false) private String category;
    private String emoji;
    @Column(nullable = false) private boolean available = true;
    protected MenuItem() {}
    public MenuItem(Restaurant restaurant, String name, String description, BigDecimal price, String category, String emoji) { this.restaurant=restaurant; this.name=name; this.description=description; this.price=price; this.category=category; this.emoji=emoji; }
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public BigDecimal getPrice(){return price;} public String getCategory(){return category;} public String getEmoji(){return emoji;} public boolean isAvailable(){return available;}
}
