package com.restaurant.ordering.dto;
import java.math.BigDecimal;
public record DashboardSummary(long pendingOrders, long readyOrders, long paidOrders, BigDecimal paidSales) {}
