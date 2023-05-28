package com.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(schema = "PUBLIC", name = "DISH")
@Data
public class Dish {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISH_ID")
    private Integer dishId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @JsonIgnore
    @Column(name = "IS_AVAILABLE")
    private Boolean isAvailable;
    @JsonIgnore
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;
    @JsonIgnore
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
}
