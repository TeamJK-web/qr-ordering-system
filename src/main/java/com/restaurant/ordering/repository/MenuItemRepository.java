package com.restaurant.ordering.repository;
import com.restaurant.ordering.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> { List<MenuItem> findByRestaurantSlugAndAvailableTrueOrderByCategoryAscNameAsc(String slug); Optional<MenuItem> findByIdAndRestaurantSlugAndAvailableTrue(Long id, String slug); }
