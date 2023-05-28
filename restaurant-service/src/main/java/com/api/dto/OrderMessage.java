package com.api.dto;

import com.api.util.EStatus;

public class OrderMessage extends Message{
    public OrderMessage(EStatus status, String message, Integer orderId) {
        super(status, message);
        this.orderId = orderId;
    }
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }
}
