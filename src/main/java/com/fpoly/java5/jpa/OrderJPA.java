package com.fpoly.java5.jpa;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fpoly.java5.entities.Order;

public interface OrderJPA extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o ORDER BY o.id DESC")
    Page<Order> findAllOrderByIdDesc(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE (:startDate IS NULL OR o.createAt >= :startDate) AND (:endDate IS NULL OR o.createAt <= :endDate) AND  o.orderStatus.id = :status")
    Page<Order> findAllOrderByDateOrOrderStatus(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("status") Integer status,
            Pageable pageable);

}
