package com.fpoly.java5.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.fpoly.java5.beans.OrderBean;
import com.fpoly.java5.entities.Cart;
import com.fpoly.java5.entities.CartItem;
import com.fpoly.java5.entities.Order;
import com.fpoly.java5.entities.OrderDetail;
import com.fpoly.java5.entities.OrderStatus;
import com.fpoly.java5.entities.Product;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.CartItemJPA;
import com.fpoly.java5.jpa.OrderDetailJPA;
import com.fpoly.java5.jpa.OrderJPA;
import com.fpoly.java5.jpa.ProductJPA;
import com.fpoly.java5.utils.OrderCalculator;
import com.fpoly.java5.utils.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class OrderController {
    @Autowired
    private OrderJPA orderJPA;
    @Autowired
    private OrderDetailJPA orderDetailJPA;
    @Autowired
    private CartItemJPA cartItemJPA;
    @Autowired
    private ProductJPA productJPA;
    @Autowired
    private UserUtil userUtil;
    @GetMapping("/user/list-ordered")
    public String getListOrderPage(Model model) {
        model.addAttribute("pageTitle", "Danh sách đơn hàng");
        model.addAttribute("namePage", "Danh sách đơn hàng");
        
        Optional<User> userIslogin = userUtil.getUserCookie();
        
        if (userIslogin.isPresent()) {
            List<Order> listOrderByUser = userIslogin.get().getOrders();
            listOrderByUser.sort(Comparator.comparing(Order::getId).reversed());
            OrderCalculator calculator = new OrderCalculator(); 
             List<Double> totalPrices = new ArrayList<>();
            
            for (Order order : listOrderByUser) {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                double totalPrice = calculator.calculateTotalPrice(orderDetails,order.getId()); 
                totalPrices.add(totalPrice);
                
            }
            
            model.addAttribute("totalPrices", totalPrices);
            model.addAttribute("listOrderByUser", listOrderByUser);
        }
    
        return "user/order";
    }
    
    

    @PostMapping("/user/order")
    public String postOrder(Model moel, OrderBean orderBean) {

        Optional<User> userIslogin = userUtil.getUserCookie();
        if (userIslogin.isPresent()) {

            Order order = new Order();
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setId(1);
            order.setUser(userIslogin.get());
            order.setFullName(orderBean.getFullName());
            order.setPhone(orderBean.getPhone());
            order.setAddress(orderBean.getAddress());
            order.setCreateAt(new Date());
            order.setOrderStatus(orderStatus);
            Order ordersave = orderJPA.save(order);

            Cart userCart = userIslogin.get().getCart();
            List<CartItem> cartItems = userCart.getCartItems();
            for (CartItem cartItem : cartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(ordersave);
                Optional<Product> product = productJPA.findById(cartItem.getProduct().getId());
                if (product.isPresent()) {
                    orderDetail.setProduct(product.get());
                    orderDetail.setQuantity(cartItem.getQuantity());
                    orderDetail.setPrice(product.get().getPrice());
                    orderDetailJPA.save(orderDetail);
                    cartItemJPA.delete(cartItem);
                    System.out.println("=================================================");
                }
            }
        }
        return "redirect:/user/cart";
    }


    @PostMapping("/user/list-ordered/remove")
    public String removeOrder(Model model,@RequestParam("id") Integer id,RedirectAttributes redirectAttributes) {
        OrderStatus status=new OrderStatus();
        status.setId(2);
        Optional<Order> order=orderJPA.findById(id);
        if (order.isPresent()) {
            if (order.get().getOrderStatus().getId()==1) {
                order.get().setOrderStatus(status);
                orderJPA.save(order.get());
                redirectAttributes.addFlashAttribute("message", "Hủy đơn thành công");
            }else if(order.get().getOrderStatus().getId()>=3){
                redirectAttributes.addFlashAttribute("message", "Không thể hủy đơn hàng vì trạng thái không cho phép.");
                System.out.println("Không thể hủy đơn hàng vì trạng thái không cho phép.");
            }
          
        }
        return "redirect:/user/list-ordered";
    }
    
}
