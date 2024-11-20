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

import com.fpoly.java5.beans.ProfileBean;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.UserJPA;
import com.fpoly.java5.utils.UserUtil;

import jakarta.validation.Valid;

@Controller
public class ProfileController {
    @Autowired
    UserJPA userJPA;
    @Autowired
    UserUtil userUtil;


    @GetMapping("/user/profile")
    public String fromProfilePage(Model model, ProfileBean profileBean) {
        model.addAttribute("pageTitle", "Thông tin người dùng");
        Optional<User> userIslogin = userUtil.getUserCookie();
        if (userIslogin.isPresent()) {
            model.addAttribute("profileBean", userIslogin.get());
        }
        return "user/profile";
    }

    @PostMapping("/user/profile")
    public String postMethodName(Model model, @Valid @ModelAttribute("profileBean") ProfileBean profileBean,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<User> user = userJPA.findById(profileBean.getId());
        if (user.isPresent()) {
            Optional<User> checkEmail = userJPA.findByEmailExist(user.get().getId(), profileBean.getEmail());
            if (checkEmail.isPresent()) {
                bindingResult.rejectValue("email", "error.profileBean", "Email đã được sử dụng");
            }
            Optional<User> checkPhone = userJPA.findByPhoneExist(user.get().getId(), profileBean.getPhone());
            if (checkPhone.isPresent()) {
                bindingResult.rejectValue("phone", "error.profileBean", "Số điện thoại đã được sử dụng");
            }
            if (bindingResult.hasErrors()) {
                return "user/profile";
            } else {
                user.get().setName(profileBean.getName());
                user.get().setEmail(profileBean.getEmail());
                user.get().setPhone(profileBean.getPhone());
                user.get().setAddress(profileBean.getAddress());
                redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
                userJPA.save(user.get());
                return "redirect:/user/profile";
            }

        } else {
            return "redirect:/login";
        }
    }


    @GetMapping("/admin/profile")
    public String fromProfilePage1(Model model, ProfileBean profileBean) {
        model.addAttribute("pageTitle", "Thông tin người dùng");
        model.addAttribute("namePage", "Thông tin người dùng");
        Optional<User> userIslogin = userUtil.getUserCookie();
        if (userIslogin.isPresent()) {
            model.addAttribute("profileBean", userIslogin.get());
        }
        return "user/profile";
    }

    @PostMapping("/admin/profile")
    public String postMethodName1(Model model, @Valid @ModelAttribute("profileBean") ProfileBean profileBean,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<User> user = userJPA.findById(profileBean.getId());
        if (user.isPresent()) {
            Optional<User> checkEmail = userJPA.findByEmailExist(user.get().getId(), profileBean.getEmail());
            if (checkEmail.isPresent()) {
                bindingResult.rejectValue("email", "error.profileBean", "Email đã được sử dụng");
            }
            Optional<User> checkPhone = userJPA.findByPhoneExist(user.get().getId(), profileBean.getPhone());
            if (checkPhone.isPresent()) {
                bindingResult.rejectValue("phone", "error.profileBean", "Số điện thoại đã được sử dụng");
            }
            if (bindingResult.hasErrors()) {
                return "user/profile";
            } else {
                user.get().setName(profileBean.getName());
                user.get().setEmail(profileBean.getEmail());
                user.get().setPhone(profileBean.getPhone());
                user.get().setAddress(profileBean.getAddress());
                redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
                userJPA.save(user.get());
                return "redirect:/admin/profile";
            }

        } else {
            return "redirect:/login";
        }
    }
}
