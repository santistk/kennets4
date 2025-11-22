package com.santistk.servicioswebapi.repositories;

import com.santistk.servicioswebapi.models.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@ApplicationScoped
public class OrderRepository extends BaseRepository<Order, Long> {
    
    @Override
    protected Class<Order> entity() {
        return Order.class;
    }

    public List<Order> findByCustomerId(Long customerId) {
        return entityManager.createQuery(
            "SELECT o FROM Order o WHERE o.customer.customerId = :customerId ORDER BY o.orderDate DESC", Order.class)
            .setParameter("customerId", customerId)
            .getResultList();
    }

    public List<Order> findByStatus(Order.OrderStatus status) {
        return entityManager.createQuery(
            "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.orderDate DESC", Order.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public void delete(Order order) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            order.getCustomer().getOrders().remove(order);
            entityManager.remove(order);
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }
}
