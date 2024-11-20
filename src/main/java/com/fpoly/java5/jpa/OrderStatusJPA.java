package com.fpoly.java5.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.java5.entities.OrderStatus;

public interface OrderStatusJPA extends JpaRepository<OrderStatus,Integer> {

}
