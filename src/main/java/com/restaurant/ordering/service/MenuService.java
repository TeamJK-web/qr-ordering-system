package com.restaurant.ordering.service;

import com.restaurant.ordering.model.MenuItem;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuService {

    private final List<MenuItem> menuItems = Arrays.asList(
        // Appetizers
        new MenuItem(1, "Spring Rolls", "Crispy vegetable spring rolls with sweet chili sauce", 120.00, "Appetizers", "🥟"),
        new MenuItem(2, "Chicken Wings", "6 pcs buffalo wings with ranch dip", 185.00, "Appetizers", "🍗"),
        new MenuItem(3, "Soup of the Day", "Ask your waiter for today's special soup", 95.00, "Appetizers", "🍲"),

        // Main Course
        new MenuItem(4, "Grilled Chicken", "Marinated chicken breast with herbs, served with rice and veggies", 280.00, "Main Course", "🍽️"),
        new MenuItem(5, "Beef Steak", "200g tenderloin steak with mushroom gravy and mashed potato", 450.00, "Main Course", "🥩"),
        new MenuItem(6, "Pork Sinigang", "Pork ribs in tamarind broth with fresh vegetables", 245.00, "Main Course", "🍜"),
        new MenuItem(7, "Seafood Pasta", "Linguine with shrimp, squid, and clams in white wine sauce", 320.00, "Main Course", "🍝"),
        new MenuItem(8, "Vegetable Stir Fry", "Seasonal vegetables in savory oyster sauce, served with rice", 180.00, "Main Course", "🥦"),

        // Rice & Noodles
        new MenuItem(9, "Fried Rice", "Egg fried rice with mixed vegetables and soy sauce", 110.00, "Rice & Noodles", "🍚"),
        new MenuItem(10, "Pancit Canton", "Stir-fried noodles with chicken, vegetables, and quail eggs", 150.00, "Rice & Noodles", "🍜"),
        new MenuItem(11, "Garlic Rice", "Fragrant garlic fried rice", 75.00, "Rice & Noodles", "🍚"),

        // Beverages
        new MenuItem(12, "Mango Juice", "Fresh squeezed mango juice", 80.00, "Beverages", "🥭"),
        new MenuItem(13, "Iced Tea", "Sweetened black iced tea with lemon", 65.00, "Beverages", "🧋"),
        new MenuItem(14, "Bottled Water", "500ml mineral water", 35.00, "Beverages", "💧"),
        new MenuItem(15, "Soda", "Can of your choice: Coke, Sprite, Royal", 55.00, "Beverages", "🥤"),

        // Desserts
        new MenuItem(16, "Halo-Halo", "Classic Filipino shaved ice dessert with leche flan and ube", 130.00, "Desserts", "🍧"),
        new MenuItem(17, "Leche Flan", "Smooth caramel custard", 95.00, "Desserts", "🍮"),
        new MenuItem(18, "Ice Cream", "2 scoops of your choice: vanilla, chocolate, or strawberry", 85.00, "Desserts", "🍦")
    );

    public List<MenuItem> getAllItems() {
        return menuItems;
    }
}
