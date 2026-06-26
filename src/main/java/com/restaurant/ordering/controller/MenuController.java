package com.restaurant.ordering.controller;

import com.restaurant.ordering.model.MenuItem;
import com.restaurant.ordering.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu")
    public List<MenuItem> getMenu() {
        return menuService.getAllItems();
    }
}
