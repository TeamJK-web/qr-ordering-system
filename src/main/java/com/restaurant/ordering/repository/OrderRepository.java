package com.restaurant.ordering.repository;
import com.restaurant.ordering.model.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface OrderRepository extends JpaRepository<Order, Long> { @Query("select distinct o from Order o left join fetch o.items where o.restaurant.slug=:slug and o.status in :statuses order by o.placedAt asc") List<Order> findKitchenOrders(@Param("slug") String slug, @Param("statuses") Collection<Order.Status> statuses); Optional<Order> findByIdAndRestaurantSlug(Long id, String slug); }
