package com.restaurant.ordering.model;

import jakarta.persistence.*;

@Entity
@Table(name="dining_tables", uniqueConstraints=@UniqueConstraint(columnNames={"restaurant_id","table_number"}))
public class DiningTable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="restaurant_id") private Restaurant restaurant;
    @Column(name="table_number", nullable=false, length=20) private String tableNumber;
    @Column(nullable=false) private boolean active=true;
    protected DiningTable() {}
    public DiningTable(Restaurant restaurant,String tableNumber){this.restaurant=restaurant;this.tableNumber=tableNumber;}
    public void setActive(boolean active){this.active=active;}
    public Long getId(){return id;} public String getTableNumber(){return tableNumber;} public boolean isActive(){return active;}
}
