package com.fpoly.java5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.fpoly.java5.utils.Contants;
import com.fpoly.java5.utils.CookieUtil;

@Controller
public class LogoutController {
    @Autowired
    private CookieUtil cookieUtil;
   @GetMapping("/logout")
    public String logout() {
        cookieUtil.remove(Contants.COOKIE_USER_ID);
        cookieUtil.remove(Contants.COOKIE_USER_ROLE);
        return "redirect:/login";
    }
}
