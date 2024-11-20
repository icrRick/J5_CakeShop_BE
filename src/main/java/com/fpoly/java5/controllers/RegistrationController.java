package com.fpoly.java5.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.java5.beans.RegisterBean1;
import com.fpoly.java5.beans.RegisterBean2;
import com.fpoly.java5.entities.Cart;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.CartJPA;
import com.fpoly.java5.jpa.UserJPA;
import com.fpoly.java5.utils.Contants;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "registerBean1", "registerBean2" })
public class RegistrationController {
    @Autowired
    UserJPA userJPA;
    @Autowired
    CartJPA cartJPA;
    @Autowired
    HttpSession session;

    @ModelAttribute("registerBean1")
    public RegisterBean1 registerBean1() {
        return new RegisterBean1();
    }

    @ModelAttribute("registerBean2")
    public RegisterBean2 registerBean2() {
        return new RegisterBean2();
    }

    @GetMapping("/register/step1")
    public String showRegistrationForm1(Model model) {
        model.addAttribute("pageTitle", "Đăng ký tài khoản");
        return "user/register1";
    }

    @GetMapping("/register/step2")
    public String showRegistrationForm2(Model model) {
        model.addAttribute("pageTitle", "Đăng ký tài khoản");
        return "user/register2";
    }

   @PostMapping("/register/step1")
public String processRegistration(
        @Valid @ModelAttribute("registerBean1") RegisterBean1 registerBean1,
        BindingResult bindingResult1,
        Model model,
        RedirectAttributes redirectAttributes) {
            model.addAttribute("pageTitle", "Đăng ký tài khoản");
    Optional<User> checkUsername = userJPA.findByUsername(registerBean1.getUsername());
    if (checkUsername.isPresent()) {
        bindingResult1.rejectValue("username", "error.registerBean1", "Tên tài khoản đã được sử dụng");
    }
    Optional<User> checkEmail = userJPA.findByEmail(registerBean1.getEmail());
    if (checkEmail.isPresent()) {
        bindingResult1.rejectValue("email", "error.registerBean1", "Email đã được sử dụng");
    }
    if (!registerBean1.getConfirmpass().equals(registerBean1.getPassword())) {
        bindingResult1.rejectValue("confirmpass", "error.registerBean1", "Mật khẩu không trùng khớp");
    }
    
    if (bindingResult1.hasErrors()) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerBean1", bindingResult1);
        redirectAttributes.addFlashAttribute("registerBean1", registerBean1);
        return "redirect:/register/step1"; 
    } else {
        return "redirect:/register/step2";
    }
}


@PostMapping("/register/step2")
public String processRegistration2(
        @Valid @ModelAttribute("registerBean2") RegisterBean2 registerBean2,
        BindingResult bindingResult2,
        Model model, 
        @ModelAttribute("registerBean1") RegisterBean1 registerBean1) {
            model.addAttribute("pageTitle", "Đăng ký tài khoản");
    Optional<User> checkPhone = userJPA.findByPhone(registerBean2.getPhone());
    if (checkPhone.isPresent()) {
        bindingResult2.rejectValue("phone", "error.registerBean2", "Số điện thoại đã được sử dụng");
    }
    if (bindingResult2.hasErrors()) {
        return "user/register2";
    } else {
        User user = new User();
        user.setName(registerBean2.getName());
        user.setPhone(registerBean2.getPhone());
        user.setAddress(registerBean2.getAddress());
        user.setUsername(registerBean1.getUsername());
        user.setPassword(registerBean1.getPassword());
        user.setEmail(registerBean1.getEmail());
        user.setRoles(Contants.USER_ROLES);
        user.setActive(true);
        User savedUser =userJPA.save(user);
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartJPA.save(cart);
        model.asMap().remove("registerBean1");
        model.asMap().remove("registerBean2");
        return "redirect:/login";
    }
}

}
