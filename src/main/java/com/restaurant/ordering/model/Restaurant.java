package com.restaurant.ordering.model;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true, length = 80) private String slug;
    protected Restaurant() {}
    public Restaurant(String name, String slug) { this.name = name; this.slug = slug; }
    public Long getId() { return id; } public String getName() { return name; } public String getSlug() { return slug; }
}
