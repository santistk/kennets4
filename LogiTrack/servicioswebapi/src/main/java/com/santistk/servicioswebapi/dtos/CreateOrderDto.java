package com.santistk.servicioswebapi.dtos;

import java.time.LocalDate;
import java.util.List;

public class CreateOrderDto {
    private Long customerId;
    private LocalDate orderDate;
    private List<OrderItemDto> items;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
