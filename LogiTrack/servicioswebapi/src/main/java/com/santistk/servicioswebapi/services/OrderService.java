package com.santistk.servicioswebapi.services;

import com.santistk.servicioswebapi.dtos.CreateOrderDto;
import com.santistk.servicioswebapi.dtos.OrderDto;
import com.santistk.servicioswebapi.dtos.OrderItemDto;
import com.santistk.servicioswebapi.models.*;
import com.santistk.servicioswebapi.repositories.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {
    
    @Inject
    private OrderRepository orderRepository;

    @Inject
    private CustomerService customerService;

    @Inject
    private ProductService productService;

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAll() {
        return orderRepository.getAll();
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Optional<OrderDto> createOrder(CreateOrderDto createOrderDto) {
        Optional<Customer> customerOpt = customerService.findById(createOrderDto.getCustomerId());
        if (!customerOpt.isPresent() || !customerOpt.get().getActive()) {
            return Optional.empty();
        }

        Order order = new Order();
        order.setCustomer(customerOpt.get());
        order.setOrderDate(createOrderDto.getOrderDate());
        order.setStatus(Order.OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDto itemDto : createOrderDto.getItems()) {
            Optional<Product> productOpt = productService.findById(itemDto.getProductId());
            if (!productOpt.isPresent() || !productOpt.get().getActive()) {
                return Optional.empty();
            }

            Product product = productOpt.get();
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(product.getPrice());
            
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            item.setSubtotal(subtotal);
            
            order.getItems().add(item);
            total = total.add(subtotal);
        }

        order.setTotalAmount(total);

        Optional<Order> savedOrder = orderRepository.save(order);
        return savedOrder.map(this::convertToDto);
    }

    public Optional<Order> updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        
        List<OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProduct().getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setSubtotal(item.getSubtotal());
            return itemDto;
        }).collect(Collectors.toList());
        
        dto.setItems(itemDtos);
        return dto;
    }
}
