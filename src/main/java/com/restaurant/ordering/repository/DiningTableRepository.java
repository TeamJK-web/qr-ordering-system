package com.restaurant.ordering.repository;
import com.restaurant.ordering.model.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DiningTableRepository extends JpaRepository<DiningTable,Long> { List<DiningTable> findByRestaurantSlugOrderByTableNumberAsc(String slug); Optional<DiningTable> findByRestaurantSlugAndTableNumberAndActiveTrue(String slug,String tableNumber); Optional<DiningTable> findByIdAndRestaurantSlug(Long id,String slug); boolean existsByRestaurantSlugAndTableNumber(String slug,String tableNumber); }
