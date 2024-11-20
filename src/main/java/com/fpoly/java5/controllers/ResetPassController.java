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

import com.fpoly.java5.beans.ResetPass;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.UserJPA;
import com.fpoly.java5.utils.Contants;
import com.fpoly.java5.utils.CookieUtil;
import com.fpoly.java5.utils.UserUtil;

import jakarta.validation.Valid;

@Controller
public class ResetPassController {
    @Autowired
    private UserJPA userJPA;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private CookieUtil cookieUtil;

    @GetMapping("/user/resetpass")
    public String getForm(Model model) {
        model.addAttribute("pageTitle", "Đổi mật khẩu");
        model.addAttribute("resetPass", new ResetPass());
        return "user/resetpass";
    }

    @PostMapping("/user/resetpass")
    public String postForm(Model model, @Valid @ModelAttribute("resetPass") ResetPass resetPass, BindingResult result,
            RedirectAttributes redirectAttributes) {
        Optional<User> userIslogin = userUtil.getUserCookie();

        if (userIslogin.isPresent()) {
            User user = userIslogin.get();
            if (result.hasErrors()) {
                return "user/resetpass";
            }
            if (!user.getPassword().equals(resetPass.getOldpass())) {
                result.rejectValue("oldpass", "error.oldpass", "Mật khẩu cũ không đúng");
            }

            if (!resetPass.getNewpass().equals(resetPass.getCfpass())) {
                result.rejectValue("cfpass", "error.cfpass", "Mật khẩu xác nhận không khớp");
            }
            if (result.hasErrors()) {
                return "user/resetpass";
            }
            user.setPassword(resetPass.getNewpass());
            redirectAttributes.addFlashAttribute("message", "Mật khẩu bạn đã được cập nhật vui lòng đăng nhập lại");
            userJPA.save(user);
            cookieUtil.remove(Contants.COOKIE_USER_ID);
            cookieUtil.remove(Contants.COOKIE_USER_ROLE);
            return "redirect:/login";
        }

        return "user/resetpass";
    }
    @GetMapping("/admin/resetpass")
    public String getForm1(Model model) {
        model.addAttribute("pageTitle", "Đổi mật khẩu");
        model.addAttribute("resetPass", new ResetPass());
        return "user/resetpass";
    }

    @PostMapping("/admin/resetpass")
    public String postForm1(Model model, @Valid @ModelAttribute("resetPass") ResetPass resetPass, BindingResult result,
            RedirectAttributes redirectAttributes) {
        Optional<User> userIslogin = userUtil.getUserCookie();

        if (userIslogin.isPresent()) {
            User user = userIslogin.get();
            if (result.hasErrors()) {
                return "user/resetpass";
            }
            if (!user.getPassword().equals(resetPass.getOldpass())) {
                result.rejectValue("oldpass", "error.oldpass", "Mật khẩu cũ không đúng");
            }

            if (!resetPass.getNewpass().equals(resetPass.getCfpass())) {
                result.rejectValue("cfpass", "error.cfpass", "Mật khẩu xác nhận không khớp");
            }
            if (result.hasErrors()) {
                return "user/resetpass";
            }
            user.setPassword(resetPass.getNewpass());
            redirectAttributes.addFlashAttribute("message", "Mật khẩu bạn đã được cập nhật vui lòng đăng nhập lại");
            userJPA.save(user);
            cookieUtil.remove(Contants.COOKIE_USER_ID);
            cookieUtil.remove(Contants.COOKIE_USER_ROLE);
            return "redirect:/login";
        }

        return "user/resetpass";
    }
}
