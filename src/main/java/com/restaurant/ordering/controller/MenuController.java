package com.restaurant.ordering.controller;
import com.restaurant.ordering.model.MenuItem;
import com.restaurant.ordering.service.MenuService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/restaurants/{restaurantSlug}") @CrossOrigin
public class MenuController { private final MenuService menu; public MenuController(MenuService menu){this.menu=menu;} @GetMapping("/menu") public List<MenuItem> getMenu(@PathVariable String restaurantSlug){return menu.getAllItems(restaurantSlug);} }
