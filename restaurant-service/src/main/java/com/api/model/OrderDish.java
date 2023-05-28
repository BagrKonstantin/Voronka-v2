package com.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(schema = "PUBLIC", name = "ORDER_DISH")
@Data
@NoArgsConstructor
public class OrderDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_DISH_ID")
    private Integer orderDishId;

    @OneToOne
    @JoinColumn(name = "DISH_ID")
    private Dish dish;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

    public OrderDish(Dish dish, Integer quantity) {
        this.dish = dish;
        this.quantity = quantity;
        this.price = dish.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
