package com.fpoly.java5.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.java5.entities.Order;
import com.fpoly.java5.entities.OrderDetail;
import com.fpoly.java5.entities.OrderStatus;
import com.fpoly.java5.entities.Product;
import com.fpoly.java5.jpa.OrderJPA;
import com.fpoly.java5.jpa.OrderStatusJPA;
import com.fpoly.java5.jpa.ProductJPA;
import com.fpoly.java5.utils.OrderCalculator;

@Controller
public class ManageOrderController {

    @Autowired
    private OrderJPA orderJPA;
    @Autowired
    private OrderStatusJPA orderStatusJPA;
    @Autowired
    private ProductJPA productJPA;

    @GetMapping("/admin/list-order")
    public String statisticalOrderPage(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("pageTitle", "Thống kê hóa đơn");
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Order> listPageOrder = orderJPA.findAllOrderByIdDesc(pageable);
        OrderCalculator calculator = new OrderCalculator();
        List<Double> totalPrices = new ArrayList<>();

        for (Order order : listPageOrder) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            double totalPrice = calculator.calculateTotalPrice(orderDetails, order.getId());
            totalPrices.add(totalPrice);
        }
        double totalPrice=0;
        for (Double tt : totalPrices) {
            totalPrice+=tt;
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalPrices", totalPrices);
        model.addAttribute("listPageOrder", listPageOrder);

        model.addAttribute("viewPage", "fragment/order/pagi_order");
        return "admin/list-order";
    }

    @GetMapping("/admin/filter-order")
    public String getMethodName(Model model,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = ISO.DATE) Optional<Date> startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = ISO.DATE) Optional<Date> endDate,
            @RequestParam(name = "status",required = false) Integer status,
            @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("pageTitle", "Thống kê hóa đơn");
        Pageable pageable = PageRequest.of(page - 1, 10);

        if (!startDate.isPresent()&&!startDate.isPresent() && status==null) {
            return "redirect:/admin/list-order";
        }

        Page<Order> listPageOrder = orderJPA.findAllOrderByDateOrOrderStatus(
                startDate.orElse(null),
                endDate.orElse(null), status, pageable);
        OrderCalculator calculator = new OrderCalculator();
        List<Double> totalPrices = new ArrayList<>();
        for (Order order : listPageOrder) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            double totalPrice = calculator.calculateTotalPrice(orderDetails, order.getId());
            totalPrices.add(totalPrice);

        }
        double totalPrice=0;
        for (Double tt : totalPrices) {
            totalPrice+=tt;
        }
      

        model.addAttribute("status", status);
        model.addAttribute("listPageOrder", listPageOrder);
        model.addAttribute("totalPrices", totalPrices);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("startDate", startDate.orElse(null));
        model.addAttribute("endDate", endDate.orElse(null));

        // model.addAttribute("message", listPageOrder.isEmpty() ? "Không tìm thấy" : "Danh sách đã được load");
    
        model.addAttribute("viewPage", "fragment/order/pagi_filterorder");
        return "admin/list-order";
    }
    @PostMapping("/admin/confirm-orderStatus")
    public String postConfirmOrderStatus(Model model, 
                                          @RequestParam("id") Integer id,
                                          @RequestParam("status") Integer status, 
                                          RedirectAttributes redirectAttributes) {
        Optional<Order> opOrder = orderJPA.findById(id);
   
        if (opOrder.isPresent()) {
            Optional<OrderStatus> opOrderStatus = orderStatusJPA.findById(status);       
            if (opOrderStatus.isPresent()) {

                opOrder.get().setOrderStatus(opOrderStatus.get());
                orderJPA.save(opOrder.get());
                if (opOrder.get().getOrderStatus().getId() >= 3) {
                    boolean sufficientStock = true; 
                    List<String> nameproduct=new ArrayList<>();
                    for (OrderDetail orderDetail : opOrder.get().getOrderDetails()) {
                        Product product = orderDetail.getProduct();
                        int newQuantity = product.getQuantity() - orderDetail.getQuantity();
                        if (newQuantity < 0) {
                            sufficientStock = false;
                            nameproduct.add(product.getName());
                            // break; 
                        }
                    }
                    if (sufficientStock) {

                        for (OrderDetail orderDetail : opOrder.get().getOrderDetails()) {
                            Product product = orderDetail.getProduct();
                            int newQuantity = product.getQuantity() - orderDetail.getQuantity();
                            product.setQuantity(newQuantity);
                            productJPA.save(product);
                        }
                     
                        redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái thành công");
                    } else {
                        opOrder.get().setOrderStatus(orderStatusJPA.findById(1).get());
                        orderJPA.save(opOrder.get());
                        redirectAttributes.addFlashAttribute("message", "Số lượng sản phẩm "+String.join(", ", nameproduct)+" không đủ để duyệt hóa đơn ");
                    }
                }
            }
           
          
        }
    
        return "redirect:/admin/list-order";
    }
    
}
