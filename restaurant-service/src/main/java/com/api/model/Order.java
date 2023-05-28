package com.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "PUBLIC", name = "ORDERS")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Integer orderId;

    @JsonIgnore
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SPECIAL_REQUESTS")
    private String specialRequests;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

//    @Column(name = "UPDATED_AT")
//    private Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.now());

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDish> dishes = new ArrayList<>();

    public void addDish(OrderDish dish) {
        dish.setOrder(this);
        dishes.add(dish);
    }
}
