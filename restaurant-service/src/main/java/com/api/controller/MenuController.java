package com.api.controller;

import com.api.model.Dish;
import com.api.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class MenuController {
    DishService dishService;

    @Autowired
    MenuController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/menu")
    public List<Dish> getMenu() {
        return dishService.getAvailablePositions();
    }
}
