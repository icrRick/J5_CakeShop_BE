package com.fpoly.java5.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fpoly.java5.beans.OrderBean;
import com.fpoly.java5.entities.CartItem;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.CartItemJPA;
import com.fpoly.java5.jpa.CartJPA;
import com.fpoly.java5.jpa.ProductJPA;

import com.fpoly.java5.utils.UserUtil;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {
    @Autowired
    private CartJPA cartJPA;
    @Autowired
    private CartItemJPA cartItemJPA;
    @Autowired
    private ProductJPA productJPA;
    @Autowired
    private UserUtil userUtil;

    @GetMapping("/user/cart")
    public String cartPage(Model model, OrderBean orderBean) {
        model.addAttribute("pageTitle", "Giỏ hàng");
        try {
            Optional<User> userIslogin = userUtil.getUserCookie();
            if (userIslogin.isPresent()) {
                orderBean.setFullName(userIslogin.get().getName());
                orderBean.setPhone(userIslogin.get().getPhone());
                orderBean.setAddress(userIslogin.get().getAddress());
                List<CartItem> cartItems = cartJPA.findById(userIslogin.get().getCart().getId()).get().getCartItems();
                double totalPrice = 0;
                if (cartItems != null && !cartItems.isEmpty()) {
                    for (CartItem item : cartItems) {
                        totalPrice += item.getQuantity() * item.getProduct().getPrice();
                    }
                } else {
                    model.addAttribute("mess", "Không có sản phẩm nào có trong giỏ hàng");
                }
                model.addAttribute("cartItems", cartItems);

                model.addAttribute("totalPrice", totalPrice);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        return "user/cart";

    }

    @PostMapping("/user/cart-user/remove")
    public String remove(@RequestParam("id") Integer productId) {
        try {
            Optional<User> userIslogin = userUtil.getUserCookie();
            if (userIslogin.isPresent()) {
                List<CartItem> cartItems = cartJPA.findById(userIslogin.get().getCart().getId()).get().getCartItems();
                for (CartItem cartItem : cartItems) {
                    if (cartItem.getProduct().getId() == productId) {
                        cartItemJPA.delete(cartItem);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        return "redirect:/user/cart";
    }

    
    @PostMapping("/user/cart-user/add-to-cart")
    public String add(@RequestParam("id") Integer productId,Model model ,@RequestParam("currentUrl") String currentUrl,RedirectAttributes redirectAttributes ) {
        try {
            Optional<User> userIslogin = userUtil.getUserCookie();

            if (userIslogin.isPresent()) {
                CartItem cartItem = null;
                List<CartItem> cartItems = cartJPA.findById(userIslogin.get().getCart().getId()).get().getCartItems();
                for (CartItem cartItem2 : cartItems) {
                    if (cartItem2.getProduct().getId() == productId) {
                        cartItem = cartItem2;
                        break;
                    }
                }
                if (cartItem != null) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                } else {
                    cartItem = new CartItem();
                    cartItem.setCart(cartJPA.findById(userIslogin.get().getCart().getId()).get());
                    cartItem.setProduct(productJPA.findById(productId).get());
                    cartItem.setQuantity(1);
                }
                cartItemJPA.save(cartItem);
                redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        return "redirect:" + currentUrl;
       
    }

    

    @PostMapping("/user/cart-user/update-quantity")
    public String updateQuantity(@RequestParam("id") Integer productId,
            @RequestParam("action") String action) {
        try {
            Optional<User> userIslogin = userUtil.getUserCookie();
            if (userIslogin != null) {
                CartItem cartItem = null;
                List<CartItem> cartItems = cartJPA.findById(userIslogin.get().getCart().getId()).get().getCartItems();
                for (CartItem cartItem2 : cartItems) {
                    if (cartItem2.getProduct().getId() == productId) {
                        cartItem = cartItem2;
                        break;
                    }
                }
                if (cartItem != null) {
                    switch (action) {
                        case "increase":
                            cartItem.setQuantity(cartItem.getQuantity() + 1);
                            break;
                        case "desc":
                            if (cartItem.getQuantity() > 1) {
                                cartItem.setQuantity(cartItem.getQuantity() - 1);
                            }
                            break;
                    }
                    cartItemJPA.save(cartItem);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }

        return "redirect:/user/cart";
    }



    @PostMapping("/prodcuct-detail/add-to-cart")
    public String postAddCart(Model model, @RequestParam("id") Integer id, @RequestParam("quantity") int quantity,@RequestParam("currentUrl") String currentUrl,RedirectAttributes redirectAttributes ) {
        Optional<User> userIslogin = userUtil.getUserCookie();
        if (userIslogin.isPresent()) {
            CartItem cartItem = null;
            List<CartItem> cartItems = cartJPA.findById(userIslogin.get().getCart().getId()).get().getCartItems();
            for (CartItem cartItem2 : cartItems) {
                if (cartItem2.getProduct().getId() == id) {
                    cartItem = cartItem2;
                    break;
                }
            }
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                cartItem = new CartItem();
                cartItem.setCart(cartJPA.findById(userIslogin.get().getCart().getId()).get());
                cartItem.setProduct(productJPA.findById(id).get());
                cartItem.setQuantity(quantity);
            }
            cartItemJPA.save(cartItem);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ");
        }
        return "redirect:" + currentUrl; 
    }
}
