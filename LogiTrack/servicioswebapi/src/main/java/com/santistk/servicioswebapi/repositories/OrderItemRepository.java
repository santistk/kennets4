package com.santistk.servicioswebapi.repositories;

import com.santistk.servicioswebapi.models.OrderItem;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderItemRepository extends BaseRepository<OrderItem, Long> {
    
    @Override
    protected Class<OrderItem> entity() {
        return OrderItem.class;
    }
}
