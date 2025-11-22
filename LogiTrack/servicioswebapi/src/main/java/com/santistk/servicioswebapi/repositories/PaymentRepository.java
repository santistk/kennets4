package com.santistk.servicioswebapi.repositories;

import com.santistk.servicioswebapi.models.Payment;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PaymentRepository extends BaseRepository<Payment, Long> {
    
    @Override
    protected Class<Payment> entity() {
        return Payment.class;
    }

    public List<Payment> findByOrderId(Long orderId) {
        return entityManager.createQuery(
            "SELECT p FROM Payment p WHERE p.order.orderId = :orderId ORDER BY p.paymentDate DESC", Payment.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }
}
