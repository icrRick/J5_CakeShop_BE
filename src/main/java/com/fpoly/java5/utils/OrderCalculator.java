package com.fpoly.java5.utils;

import java.util.List;

import com.fpoly.java5.entities.OrderDetail;

public class OrderCalculator {
    public double calculateTotalPrice(List<OrderDetail> orderDetails,Integer orderId) {
        double totalPrice = 0.0; 
        for (OrderDetail detail : orderDetails) {
            totalPrice += detail.getPrice() * detail.getQuantity(); 
        }
        return totalPrice; 
    }
}
