package com.restaurant.ordering.repository;
import com.restaurant.ordering.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EmployeeRepository extends JpaRepository<Employee, Long> { Optional<Employee> findByIdAndRestaurantSlug(Long id, String slug); Optional<Employee> findFirstByRestaurantSlugAndRole(String slug, Employee.Role role); }
