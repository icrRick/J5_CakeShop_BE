package com.fpoly.java5.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fpoly.java5.beans.UserBean;
import com.fpoly.java5.entities.User;
import com.fpoly.java5.jpa.UserJPA;

import jakarta.validation.Valid;

@Controller
public class ManageUserController {
    @Autowired
    private UserJPA userJPA;

    @GetMapping("/admin/list-user")
    public String listUserPage(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("pageTitle", "Danh sách người dùng");
        Pageable pageable = PageRequest.of(page - 1, 10);
        String returnUrl = "/admin/list-user?page=" + page;
        model.addAttribute("returnUrl", returnUrl);
        Page<User> getPageAllUser = userJPA.pageAllUserByRolesFalse(pageable);
        model.addAttribute("getPageAllUser", getPageAllUser);
        model.addAttribute("viewPage", "fragment/user/pagi_user");
        return "admin/list-user";
    }

    @GetMapping("/admin/filter-user")
    public String filterUser(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Boolean status) {
        model.addAttribute("pageTitle", "Danh sách người dùng");
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<User> getPageAllUser = userJPA.pageAllFilterByRolesFalse(keyword, status, pageable);
        if (keyword.equals("") && status == null) {
            return "redirect:/admin/list-user";
        }
        model.addAttribute("getPageAllUser", getPageAllUser);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("status", status);
        model.addAttribute("returnUrl", "/admin/filter-user?keyword=" + (keyword != null ? keyword : "")
                + "&status?=" + status + "&page" + page);
        model.addAttribute("viewPage", "fragment/user/pagi_filteruser");

        return "admin/list-user";
    }

    @PostMapping("/admin/remove-user")
    public String removeUser(Model model, @RequestParam("returnUrl") String returnUrl,
            @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        Optional<User> user = userJPA.findById(id);
        if (user.isPresent()) {
            user.get().setActive(false);
            userJPA.save(user.get());
            redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        }
        return "redirect:" + returnUrl;

    }

    @GetMapping("/admin/form-user")
    public String fromCetagoryPage(Model model, @RequestParam("id") Optional<Integer> id) {
        model.addAttribute("pageTitle", "Form loại sản phẩm");
        if (id.isPresent()) {
            Optional<User> user = userJPA.findById(id.get());
            if (user.isPresent()) {
                model.addAttribute("userBean", user.isPresent() ? user.get() : null);
            } else {
                return "error/404";
            }

        } else {
            model.addAttribute("userBean", new UserBean());
        }
        return "admin/form-user";
    }

    @PostMapping("/admin/form-user")
    public String saveUser(
            Model model,
            @Valid @ModelAttribute("userBean") UserBean userBean,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("pageTitle", "Form người dùng");

        if (result.hasErrors()) {
            return "admin/form-user";
        }

        User user = new User();
        Optional<User> checkEmail = userJPA.findByEmailExist(userBean.getId(), userBean.getEmail());
        if (checkEmail.isPresent()) {
            result.rejectValue("email", "error.userBean", "Email đã được sử dụng");
        }
        Optional<User> checkPhone = userJPA.findByPhoneExist(userBean.getId(), userBean.getPhone());
        if (checkPhone.isPresent()) {
            result.rejectValue("phone", "error.userBean", "Số điện thoại đã được sử dụng");
        }
        if (result.hasErrors()) {
            return "admin/form-user";
        }
        if (userBean.getId() != null) {
            Optional<User> optionalUser = userJPA.findById(userBean.getId());
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
        }

        user.setName(userBean.getName());
        user.setEmail(userBean.getEmail());
        user.setPhone(userBean.getPhone());
        user.setAddress(userBean.getAddress());
        user.setActive(userBean.getActive());

        userJPA.save(user);
        redirectAttributes.addFlashAttribute("message",
                userBean.getId() != null ? "Cập nhật thành công" : "Thêm thành công");
        return "redirect:/admin/list-user";
    }
}
