package com.restaurant.ordering.dto;
import java.math.BigDecimal;
public record MenuItemRequest(String name, String description, BigDecimal price, String category, String emoji, boolean available) {}
