package com.fpoly.java5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fpoly.java5.jpa.ProductJPA;



@Controller
public class MenuController {
    @Autowired
    private ProductJPA g_ProductJPA;
    @GetMapping("/menu")
    public String getMenuPage(Model model) {
        model.addAttribute("pageTitle", "Thự đơn");
        model.addAttribute("namePage", "Thực đơn");
        model.addAttribute("products", g_ProductJPA.findAllWithActive(true));
        return "views/menu";
    }
    
}
