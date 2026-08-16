package com.restaurant.ordering.repository;
import com.restaurant.ordering.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> { Optional<Restaurant> findBySlug(String slug); }
