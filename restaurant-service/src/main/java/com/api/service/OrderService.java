package com.api.service;

import com.api.util.EStatus;
import com.api.dto.OrderRequestDTO;
import com.api.dto.Message;
import com.api.dto.OrderMessage;
import com.api.model.Dish;
import com.api.model.Order;
import com.api.model.OrderDish;
import com.api.repository.DishRepository;
import com.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    OrderRepository orderRepository;
    DishRepository dishRepository;

    @Autowired
    OrderService(OrderRepository orderRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
    }


    public Message makeOrder(OrderRequestDTO order) {
        List<Dish> dishes = dishRepository.findAll();
        Order newOrder = new Order();
        newOrder.setUserId(order.getUserId());
        newOrder.setStatus("CREATED");
        for (var pos : order.getPositions()) {
            Optional<Dish> dish = dishes.stream().filter(d -> d.getTitle().equals(pos.getTitle())).findFirst();
            if (dish.isEmpty()) {
                return new Message(EStatus.ERROR, "Dish " + pos.getTitle() + " doesn't exist");
            }
            if (!dish.get().getIsAvailable() || dish.get().getQuantity() == 0) {
                return new Message(EStatus.ERROR, "Dish " + pos.getTitle() + " is currently unavailable");
            }
            if (dish.get().getQuantity() < pos.getQuantity()) {
                return new Message(EStatus.ERROR, "Left only " + dish.get().getQuantity() + " of " + pos.getTitle());
            }
            newOrder.addDish(new OrderDish(dish.get(), pos.getQuantity()));
            dish.get().setQuantity(dish.get().getQuantity() - pos.getQuantity());
        }

        orderRepository.save(newOrder);
        dishRepository.saveAll(dishes);
        return new OrderMessage(EStatus.OK, "Order was created", newOrder.getOrderId());
    }

    public Optional<Order> findById(Integer orderId) {
        return orderRepository.findById(orderId);
    }
}
