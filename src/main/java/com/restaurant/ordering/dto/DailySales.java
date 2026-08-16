package com.restaurant.ordering.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public record DailySales(LocalDate date, long paymentCount, BigDecimal total) {}
