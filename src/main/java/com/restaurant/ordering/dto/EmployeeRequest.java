package com.restaurant.ordering.dto;
import com.restaurant.ordering.model.Employee;
public record EmployeeRequest(String name, Employee.Role role, String pin) {}
