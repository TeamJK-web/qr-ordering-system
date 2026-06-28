package com.restaurant.ordering.model;

public class OrderItem {
    private String name;
    private String emoji;
    private int quantity;
    private double price;
    private double subtotal;

    public OrderItem() {}

    public OrderItem(String name, String emoji, int quantity, double price) {
        this.name = name;
        this.emoji = emoji;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price * quantity;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
