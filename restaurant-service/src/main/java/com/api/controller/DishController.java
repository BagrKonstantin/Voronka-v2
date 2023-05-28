package com.api.controller;

import com.api.dto.DishDTO;
import com.api.service.DishService;
import com.api.util.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/api")
public class DishController {
    DishService dishService;

    @Autowired
    DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(dishService.findAll());
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishDTO> getDish(@PathVariable final Integer dishId) {
        return ResponseEntity.ok(dishService.get(dishId));
    }

    @PostMapping
    public ResponseEntity<Integer> createDish(@RequestBody @Valid final DishDTO dishDTO) {
        final Integer createdDishId = dishService.create(dishDTO);
        return new ResponseEntity<>(createdDishId, HttpStatus.CREATED);
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<Void> updateDish(@PathVariable final Integer dishId,
                                           @RequestBody @Valid final DishDTO dishDTO) {
        dishService.update(dishId, dishDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<Void> deleteDish(@PathVariable final Integer dishId) {
        dishService.delete(dishId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundExceptions(NotFoundException ex) {
        return "Dish with such id was not found";
    }
}
