package com.fpoly.java5.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.java5.entities.Comment;
import com.fpoly.java5.entities.Product;

import com.fpoly.java5.jpa.ProductJPA;

@Controller
public class ProductDetailController {
    @Autowired
    private ProductJPA productJPA;

    @GetMapping("/productdetail")
    public String productdetailPage(Model model, @RequestParam("id") Integer id) {
        model.addAttribute("pageTitle", "Chi tiết sản phẩm");
        model.addAttribute("namePage", "Chi tiết sản phẩm");

        Optional<Product> productInfo = productJPA.findById(id);

        if (productInfo.isPresent()) {
            List<Comment> comments = productInfo.get().getComments();
            model.addAttribute("comments", comments != null ? comments : new ArrayList<>());
            model.addAttribute("productInfo", productInfo.get());
            String currentUrl = "/productdetail?id=" + id;
            model.addAttribute("currentUrl", currentUrl);
        } else {
            return "err/404";
        }

        return "views/productdetail";
    }

}
