package com.shoes.assignment.repository;

import com.shoes.assignment.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderDate = :date")
    List<Order> findOrdersByDate(@Param("date") LocalDate date);

    @Query("SELECT o.user.userId AS userId, SUM(o.totalPrice) AS totalPrice, MAX(o.orderDate) AS orderDate, MAX(o.orderId) AS orderId " +
            "FROM Order o WHERE o.orderDate = :date GROUP BY o.user.userId")
    List<Order> findOrdersByDateGroupedByUser(@Param("date") LocalDate date);
}
