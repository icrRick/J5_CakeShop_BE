package com.fpoly.java5.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import com.fpoly.java5.entities.Product;

public interface StatisticalJPA extends JpaRepository<Product, Integer>{
	
	@Transactional(readOnly = true)
    @Procedure(procedureName = "GetTotalOrdersByDateRangeAndStatus")
    List<Object[]> getTotalOrdersByDateRangeAndStatus(String startDate, String endDate, boolean isSuccess);
    
    @Transactional(readOnly = true)
    @Procedure(procedureName = "GetDailyRevenueByDateRange")
    List<Object[]> getDailyRevenueByDateRange(String startDate, String endDate);
    
    @Transactional(readOnly = true)
    @Procedure(procedureName = "GetTotalProductsSoldByDateRange ")
    List<Object[]> getTotalProductsSoldByDateRange(String startDate, String endDate);
}
