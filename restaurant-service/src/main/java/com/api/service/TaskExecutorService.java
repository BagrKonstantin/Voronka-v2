package com.api.service;

import com.api.util.EOrderStatus;
import com.api.model.Order;
import com.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@EnableScheduling
@Service
@EnableAsync
public class TaskExecutorService {
    OrderRepository orderRepository;

    @Autowired
    TaskExecutorService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    Random random = new Random(42);

    @Scheduled(fixedDelay = 10000)
    public void doSomething() throws InterruptedException {
        Optional<Order> order = orderRepository.findTopByStatusNotOrderByCreatedAtAsc(EOrderStatus.FINISHED.toString());
        if (order.isEmpty()) {
            return;
        }
        System.out.println(order.get().getOrderId());

        order.get().setStatus(EOrderStatus.COOKING.toString());
        orderRepository.save(order.get());
        System.out.println("Order is cooking");
        Thread.sleep(1000L * (10 + Math.abs(random.nextInt()) % 60));
        order.get().setStatus(EOrderStatus.FINISHED.toString());
        orderRepository.save(order.get());
        System.out.println("Order is ready");
    }

}
