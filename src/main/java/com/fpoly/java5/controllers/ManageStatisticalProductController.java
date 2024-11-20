package com.fpoly.java5.controllers;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.fpoly.java5.entities.Product;
import com.fpoly.java5.entities.Statistical;
import com.fpoly.java5.jpa.ProductJPA;
import com.fpoly.java5.jpa.StatisticalJPA;
import com.fpoly.java5.utils.DateUtil;

@Controller
public class ManageStatisticalProductController {
	@Autowired
	ProductJPA g_productJPA;

	@Autowired
	StatisticalJPA g_statisticalJPA;

	@GetMapping("/admin/statistical-product")
	@Transactional(readOnly = true)
	public String statisticalProductPage(Model model, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		model.addAttribute("pageTitle", "Thống kê sản phẩm");
		model.addAttribute("namePage", "Thống kê sản phẩm");

		String startDateString = "";
		String endDateString = "";

		String month = String.valueOf(DateUtil.getCurrentMonth() - 1);
		String year = String.valueOf(DateUtil.getCurrentYear());

		startDateString = year + "-" + month + "-1";
		endDateString = year + "-" + month + "-28";

		if (startDate != null && endDate != null) {
			startDateString = startDate;
			endDateString=endDate;
		} else if (startDate != null) {
			startDateString = startDate;
		} else if (endDate != null) {
			endDateString = endDate;
		}

		// Fetch products and categories
		List<Product> products = g_productJPA.findAllWithActive(true);

		List<Object[]> resultSuccessOrders = g_statisticalJPA.getTotalOrdersByDateRangeAndStatus(startDateString,
				endDateString, true);
		List<Object[]> resultFailureOrders = g_statisticalJPA.getTotalOrdersByDateRangeAndStatus(startDateString,
				endDateString, false);
		List<Object[]> resultRevenue = g_statisticalJPA.getDailyRevenueByDateRange(startDateString, endDateString);
		List<Object[]> resultSoldProducts = g_statisticalJPA.getTotalProductsSoldByDateRange(startDateString,
				endDateString);

		List<Statistical> soldProducts = new ArrayList<>();

		for (Object[] obj : resultSoldProducts) {
			Date date = (Date) obj[0];
			long total = ((Number) obj[1]).longValue();
			String des = (String) obj[2].toString();
			soldProducts.add(new Statistical(date, total, des));
		}
		List<Date> soldProductsDate = new ArrayList<>();
		List<Long> soldProductsEachDay = new ArrayList<>();
		List<String> soldProductsDescription = new ArrayList<>();
		for (Statistical statistical : soldProducts) {
			soldProductsDate.add(statistical.getDate());
			soldProductsEachDay.add(statistical.getTotal());
			soldProductsDescription.add(statistical.getDescription());
		}

		model.addAttribute("soldProductsDate", soldProductsDate);
		model.addAttribute("soldProductsEachDay", soldProductsEachDay);
		model.addAttribute("soldProductsDescription", soldProductsDescription);

		// doanh thu
		List<Statistical> revenue = convertListObjectToListStatistical(resultRevenue);
		List<Date> revenueDate = new ArrayList<>();
		;
		List<Long> revenueEachDay = new ArrayList<>();
		;
		addDataToListFromListStatistical(revenue, revenueDate, revenueEachDay);

		model.addAttribute("revenueDate", revenueDate);
		model.addAttribute("revenueEachDay", revenueEachDay);
		model.addAttribute("revenueTotal", sumListLong(revenueEachDay));

		// đơn thành công
		List<Statistical> successOrders = convertListObjectToListStatistical(resultSuccessOrders);
		List<Date> successOrdersDate = new ArrayList<>();
		;
		List<Long> successOrdersEachDay = new ArrayList<>();
		;
		addDataToListFromListStatistical(successOrders, successOrdersDate, successOrdersEachDay);

		model.addAttribute("successOrdersDate", successOrdersDate);
		model.addAttribute("successOrdersEachDay", successOrdersEachDay);
		model.addAttribute("successOrdersTotal", sumListLong(successOrdersEachDay));

		// đơn thất bại
		List<Statistical> failureOrders = convertListObjectToListStatistical(resultFailureOrders);
		List<Date> failureOrdersDate = new ArrayList<>();
		;
		List<Long> failureOrdersEachDay = new ArrayList<>();
		;
		addDataToListFromListStatistical(failureOrders, failureOrdersDate, failureOrdersEachDay);

		model.addAttribute("failureOrdersDate", failureOrdersDate);
		model.addAttribute("failureOrdersEachDay", failureOrdersEachDay);
		model.addAttribute("failureOrdersTotal", sumListLong(failureOrdersEachDay));

		// Prepare product name and quantity data for chart
		List<String> productsName = new ArrayList<>();
		List<Integer> productsQuantity = new ArrayList<>();
		for (Product product : products) {
			productsName.add(product.getName());
			productsQuantity.add(product.getQuantity());
		}
		// Add attributes to the model
		model.addAttribute("productsName", productsName);
		model.addAttribute("productsQuantity", productsQuantity);

		return "admin/statistical-product";
	}

	private List<Statistical> convertListObjectToListStatistical(List<Object[]> listObj) {
		List<Statistical> list = new ArrayList<>();

		for (Object[] obj : listObj) {
			Date date = (Date) obj[0];
			long total = ((Number) obj[1]).longValue();
			list.add(new Statistical(date, total));
		}

		return list;
	}

	private void addDataToListFromListStatistical(List<Statistical> statisticals, List<Date> listDate,
			List<Long> listLong) {
		for (Statistical statistical : statisticals) {
			listDate.add(statistical.getDate());
			listLong.add(statistical.getTotal());
		}
	}

	private int sumListLong(List<Long> listInt) {
		int sum = 0;
		for (int i = 0; i < listInt.size(); i++) {
			sum += listInt.get(i);
		}
		return sum;
	}
}
