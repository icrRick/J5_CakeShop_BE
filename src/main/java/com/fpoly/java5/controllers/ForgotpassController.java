package com.fpoly.java5.controllers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.java5.beans.ForgotBean;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.UserJPA;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ForgotpassController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserJPA userJPA;

    @GetMapping("/forgot-pass")
    public String getForm(Model model, @RequestParam(value = "email", required = false) String email) {
        model.addAttribute("forgotBean", new ForgotBean());
        Optional<User> checkEmail = userJPA.findByEmail(email);
        if (checkEmail.isPresent()) {
            model.addAttribute("checkEmail", checkEmail.get());
        }
        return "user/forgotpass";
    }

    @PostMapping("/forgot-pass")
    public String postForm(Model model,
            HttpSession session, RedirectAttributes redirectAttributes,
            @RequestParam(value = "email", required = false) String email) {
        Optional<User> checkEmail = userJPA.findByEmail(email);
        if (checkEmail.isPresent()) {
            int otp = (int) ((Math.random() * (999999 - 100000 + 1)) + 100000);
            session.setAttribute("otp", otp);
            session.setAttribute("otpEmail", email);
            session.setAttribute("otpExpiration", LocalDateTime.now().plusSeconds(20));

            String subject = "Mã OTP của bạn";
            String text = "Mã OTP của bạn là: " + otp + ". Vui lòng sử dụng mã này để xác nhận.";

            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(checkEmail.get().getEmail());
                message.setSubject(subject);
                message.setText(text);
                mailSender.send(message);

                redirectAttributes.addFlashAttribute("message", "Mã OTP đã được gửi đến email của bạn.");
                return "redirect:/forgot-pass?email=" + email;
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Gửi email thất bại, vui lòng thử lại.");
                return "redirect:/forgot-pass";
            }
        }
        return "user/forgotpass";
    }

    @PostMapping("/reset-pass")
    public String resetPassword(Model model, @Valid @ModelAttribute("forgotBean") ForgotBean forgotBean,
            BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {
        String sessionOtpEmail = (String) session.getAttribute("otpEmail");
        Integer sessionOtp = (Integer) session.getAttribute("otp");
        LocalDateTime otpExpiration = (LocalDateTime) session.getAttribute("otpExpiration");
        Optional<User> checkEmail = userJPA.findByEmail(sessionOtpEmail);
        if (checkEmail.isPresent()) {
            model.addAttribute("checkEmail", checkEmail.get());
            if (result.hasErrors()) {
                return "user/forgotpass";
            }

            if (sessionOtpEmail == null || sessionOtp == null || otpExpiration == null ||
                    LocalDateTime.now().isAfter(otpExpiration)) {
                result.rejectValue("otp", "error.forgotBean", "OTP không hợp lệ hoặc đã hết hạn.");
            }

            if ( sessionOtp  != forgotBean.getOtp()) {
                result.rejectValue("otp", "error.forgotBean", "OTP không chính xác.");
            }

            if (!forgotBean.getNewpass().equals(forgotBean.getCfpass())) {
                result.rejectValue("cfpass", "error.forgotBean", "Mật khẩu xác nhận không khớp.");
            }
            if (result.hasErrors()) {
                return "user/forgotpass";
            }
            User user = checkEmail.get();
            user.setPassword(forgotBean.getNewpass());
            userJPA.save(user);

            // Clear OTP from the session
            session.removeAttribute("otp");
            session.removeAttribute("otpEmail");
            session.removeAttribute("otpExpiration");

            redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được cập nhật thành công.");
            return "redirect:/home";
        }
        result.rejectValue("email", "error.forgotBean", "Không tìm thấy email.");
        return "user/forgotpass";
    }

}
