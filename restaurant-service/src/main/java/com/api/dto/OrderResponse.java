package com.api.dto;

import com.api.util.EStatus;

public class OrderResponse extends Response {
    public OrderResponse(EStatus status, String message, Integer orderId) {
        super(status, message);
        this.orderId = orderId;
    }
    private final Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }
}
