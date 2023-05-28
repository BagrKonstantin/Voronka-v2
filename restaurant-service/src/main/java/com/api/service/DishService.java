package com.api.service;

import com.api.dto.DishDTO;
import com.api.util.NotFoundException;
import com.api.model.Dish;
import com.api.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    @Autowired
    DishRepository dishRepository;

    public List<Dish> getAvailablePositions() {
        return dishRepository.findAll().stream().filter(s -> s.getIsAvailable() && s.getQuantity() > 0).toList();
    }

    public List<DishDTO> findAll() {
        final List<Dish> dishs = dishRepository.findAll(Sort.by("dishId"));
        return dishs.stream()
                .map((dish) -> mapToDTO(dish, new DishDTO()))
                .toList();
    }

    public DishDTO get(final Integer dishId) {
        return dishRepository.findById(dishId)
                .map((dish) -> mapToDTO(dish, new DishDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DishDTO dishDTO) {
        final Dish dish = new Dish();
        mapToEntity(dishDTO, dish);
        return dishRepository.save(dish).getDishId();
    }

    public void update(final Integer dishId, final DishDTO dishDTO) {
        final Dish dish = dishRepository.findById(dishId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(dishDTO, dish);
        dishRepository.save(dish);
    }

    public void delete(final Integer dishId) {
        dishRepository.deleteById(dishId);
    }

    private DishDTO mapToDTO(final Dish dish, final DishDTO dishDTO) {
        dishDTO.setDishId(dish.getDishId());
        dishDTO.setTitle(dish.getTitle());
        dishDTO.setDescription(dish.getDescription());
        dishDTO.setPrice(dish.getPrice());
        dishDTO.setQuantity(dish.getQuantity());
        dishDTO.setIsAvailable(dish.getIsAvailable());
        return dishDTO;
    }

    private Dish mapToEntity(final DishDTO dishDTO, final Dish dish) {
        dish.setTitle(dishDTO.getTitle());
        dish.setDescription(dishDTO.getDescription());
        dish.setPrice(dishDTO.getPrice());
        dish.setQuantity(dishDTO.getQuantity());
        dish.setIsAvailable(dishDTO.getIsAvailable());
        return dish;
    }
}
