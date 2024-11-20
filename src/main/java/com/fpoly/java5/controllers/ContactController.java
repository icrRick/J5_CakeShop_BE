package com.fpoly.java5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ContactController {
    @GetMapping("/contact")
    public String getContactPage(Model model) {
        model.addAttribute("pageTitle", "Liên hệ với chúng tôi");
        model.addAttribute("namePage", "Liên hệ");
        return "views/contact";
    }
    
}
