package com.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class DishDTO {

    private Integer dishId;

    @NotNull
    @Size(max = 100)
    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Min(0)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @JsonProperty("isAvailable")
    private Boolean isAvailable;

}
