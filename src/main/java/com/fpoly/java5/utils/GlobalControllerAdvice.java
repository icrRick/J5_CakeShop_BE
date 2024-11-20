package com.fpoly.java5.utils;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fpoly.java5.entities.CartItem;
import com.fpoly.java5.entities.Category;
import com.fpoly.java5.entities.OrderStatus;
import com.fpoly.java5.entities.User;

import com.fpoly.java5.jpa.CartJPA;
import com.fpoly.java5.jpa.CategoryJPA;
import com.fpoly.java5.jpa.OrderStatusJPA;


@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private CategoryJPA categoryJPA;
    @Autowired
    private OrderStatusJPA orderStatusJPA;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private CartJPA cartJPA;
    @ModelAttribute("userIslogin")
    public void addLoggedInUserToModel( Model model) {
        model.addAttribute("userIslogin", userUtil.getUserCookie().isPresent()?userUtil.getUserCookie().get():null);
      
    }
    @ModelAttribute("getAllCategoryByActive")
    public List<Category> listAllActive() {
        return categoryJPA.getAllCategoryByActive();
    }
    @ModelAttribute("listAllOrderStatus")
    public List<OrderStatus> listAllOrderStatus() {
        return orderStatusJPA.findAll();
    }
    @ModelAttribute("cartItems")
    public List<CartItem> listCartItemsByuser(){
         Optional<User> userIslogin = userUtil.getUserCookie();
         List<CartItem> cartItems=null;
         if (userIslogin.isPresent()) {
            cartItems = cartJPA.findById(userIslogin.get().getCart().getId()).get().getCartItems();
         }
         return cartItems;
    }

}