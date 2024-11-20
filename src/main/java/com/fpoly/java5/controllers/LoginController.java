package com.fpoly.java5.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.java5.beans.LoginBean;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.UserJPA;
import com.fpoly.java5.utils.Contants;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private HttpServletResponse response;
    @Autowired 
    private UserJPA userJPA;
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("pageTitle", "Đăng nhập tài khoản");
        model.addAttribute("loginBean", new LoginBean());
        return "user/login";
    }
    @PostMapping("/login")
    public String postLoginPage(Model model,@Valid @ModelAttribute("loginBean") LoginBean loginBean,BindingResult result,RedirectAttributes redirectAttributes) {
        model.addAttribute("pageTitle", "Đăng nhập tài khoản");
        if (result.hasErrors()) {
            return "user/login";
        }
        Optional<User> userLogin = userJPA.findByUsername(loginBean.getUsername());

        if (userLogin.isPresent() && userLogin.get().getPassword().equals(loginBean.getPassword())) {
            Cookie cookieUserId = new Cookie(Contants.COOKIE_USER_ID, String.valueOf(userLogin.get().getId()));
            Cookie cookieUserRoles = new Cookie(Contants.COOKIE_USER_ROLE, String.valueOf(userLogin.get().getRoles()));
            response.addCookie(cookieUserId);
            response.addCookie(cookieUserRoles);
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công");
            return "redirect:/home";
        } else {
            result.rejectValue("username", "error.loginBean", "Tài khoản hoặc mật khẩu không đúng");
            return "user/login";
        }
        
      
    }
}
