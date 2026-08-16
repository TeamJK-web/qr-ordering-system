package com.restaurant.ordering.dto;
import java.util.List;
public record PlaceOrderRequest(String tableNumber, List<Item> items) { public record Item(Long menuItemId, int quantity) {} }
