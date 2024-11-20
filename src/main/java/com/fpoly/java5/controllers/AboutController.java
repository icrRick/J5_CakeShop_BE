package com.fpoly.java5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AboutController {
    @GetMapping("/about")
    public String getAboutPage(Model model) {
        model.addAttribute("pageTitle", "Giới thiệu ");
        model.addAttribute("namePage", "Giới thiệu");
        return "views/about";
    }
    
}
