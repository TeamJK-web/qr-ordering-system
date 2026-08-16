package com.restaurant.ordering.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity @Table(name = "employees")
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "restaurant_id") private Restaurant restaurant;
    @Column(nullable = false) private String name;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;
    @JsonIgnore @Column(name = "pin_hash") private String pinHash;
    public enum Role { ADMIN, CASHIER, KITCHEN }
    protected Employee() {}
    public Employee(Restaurant restaurant, String name, Role role, String pinHash) { this.restaurant=restaurant; this.name=name; this.role=role; this.pinHash=pinHash; }
    public Long getId(){return id;} public String getName(){return name;} public Role getRole(){return role;}
}
