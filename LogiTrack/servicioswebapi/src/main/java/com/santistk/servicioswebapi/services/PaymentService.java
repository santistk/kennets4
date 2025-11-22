package com.santistk.servicioswebapi.services;

import com.santistk.servicioswebapi.dtos.PaymentDto;
import com.santistk.servicioswebapi.models.Order;
import com.santistk.servicioswebapi.models.Payment;
import com.santistk.servicioswebapi.repositories.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentService {
    
    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private OrderService orderService;

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Optional<Payment> createPayment(PaymentDto paymentDto) {
        Optional<Order> orderOpt = orderService.findById(paymentDto.getOrderId());
        if (!orderOpt.isPresent()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        
        BigDecimal totalPaid = order.getPayments().stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal remainingAmount = order.getTotalAmount().subtract(totalPaid);
        
        if (paymentDto.getAmount().compareTo(remainingAmount) > 0) {
            return Optional.empty();
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setAmount(paymentDto.getAmount());
        payment.setMethod(paymentDto.getMethod());

        return paymentRepository.save(payment);
    }

    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }
}
