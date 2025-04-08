package com.shoes.assignment.service;

import com.shoes.assignment.model.Order;
import com.shoes.assignment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public void removeOrderFromCart(Long orderId, List<Order> cart) {
        cart.removeIf(order -> order.getOrderId().equals(orderId));
    }

    public List<Order> findOrdersByDateGroupedByUser(LocalDate date) {
        return orderRepository.findOrdersByDateGroupedByUser(date);
    }
    
    
}
