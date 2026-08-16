package com.restaurant.ordering.service;
import com.restaurant.ordering.model.MenuItem;
import com.restaurant.ordering.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class MenuService { private final MenuItemRepository menuItems; public MenuService(MenuItemRepository menuItems){this.menuItems=menuItems;} public List<MenuItem> getAllItems(String restaurantSlug){return menuItems.findByRestaurantSlugAndAvailableTrueOrderByCategoryAscNameAsc(restaurantSlug);} }
