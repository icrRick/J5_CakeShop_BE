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

import com.fpoly.java5.beans.CategoryBean;
import com.fpoly.java5.entities.Category;
import com.fpoly.java5.jpa.CategoryJPA;

import jakarta.validation.Valid;

@Controller
public class ManageCategoryController {
    @Autowired
    private CategoryJPA categoryJPA;

    @GetMapping("/admin/list-category")
    public String listCetagoryPage(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("pageTitle", "Danh sách loại sản phẩm");
        Pageable pageable = PageRequest.of(page - 1, 5);
        String returnUrl = "/admin/list-category?page=" + page; // Thay đổi tên biến
        model.addAttribute("returnUrl", returnUrl);
        Page<Category> getPageAllCategory = categoryJPA.pageAllCategory(pageable);
        model.addAttribute("getPageAllCategory", getPageAllCategory);
        model.addAttribute("getAllCategory", categoryJPA.findAll());

        model.addAttribute("viewPage", "fragment/category/pagi_category");
        return "admin/list-category";
    }

    @GetMapping("/admin/filter-category")
    public String filterCategory(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Boolean status) {
        model.addAttribute("pageTitle", "Danh sách loại sản phẩm");
        Pageable pageable = PageRequest.of(page - 1, 5);

        Page<Category> getPageAllCategory = categoryJPA.filterCategoryByNameAndActive(keyword, status, pageable);
        model.addAttribute("getPageAllCategory", getPageAllCategory);
        if (keyword.equals("") && status == null) {
            return "redirect:/admin/list-category";
        }

        model.addAttribute("getAllCategory", categoryJPA.findAll());
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("status", status);
        
        model.addAttribute("returnUrl", "/admin/filter-category?keyword=" + (keyword != null ? keyword : "")
                + "&status?=" + status + "&page" + page);
        model.addAttribute("viewPage", "fragment/category/pagi_filtercategory");

        return "admin/list-category";
    }

    @PostMapping("/admin/remove-category")
    public String removeCategory(Model model, @RequestParam("returnUrl") String returnUrl,
            @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        Optional<Category> category = categoryJPA.findById(id);
        if (category.isPresent()) {
            category.get().setActive(false);
            categoryJPA.save(category.get());
            redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        }
        return "redirect:" + returnUrl;

    }

    @GetMapping("/admin/form-category")
    public String fromCetagoryPage(Model model, @RequestParam("id") Optional<Integer> id) {
        model.addAttribute("pageTitle", "Form loại sản phẩm");
        if (id.isPresent()) {
            Optional<Category> category = categoryJPA.findById(id.get());
            if (category.isPresent()) {
                model.addAttribute("categoryBean", category.isPresent() ? category.get() : null);
            } else {
                return "error/404";
            }

        } else {
            model.addAttribute("categoryBean", new CategoryBean());
        } 
        
        return "admin/form-category";
    }
    @PostMapping("/admin/form-category")
    public String saveCategory(
            Model model,
            @Valid @ModelAttribute("categoryBean") CategoryBean categoryBean,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("pageTitle", "Form loại sản phẩm");

        if (result.hasErrors()) {
            return "admin/form-category"; 
        }

        Category category = new Category();

        Optional<Category> opCategory=categoryJPA.findByNameExist(categoryBean.getId(), categoryBean.getName());
        if (opCategory.isPresent()) {
            result.rejectValue("name", "error.categoryBean", "Tên loại sản phẩm đã tồn tại");
            return "admin/form-category"; 
        }
        if (categoryBean.getId() != null) {
            Optional<Category> optionalCategory = categoryJPA.findById(categoryBean.getId());
            if (optionalCategory.isPresent()) {
                category = optionalCategory.get(); 
            }
        } 
        category.setName(categoryBean.getName());
        category.setActive(categoryBean.getActive());

        categoryJPA.save(category);
        redirectAttributes.addFlashAttribute("message", categoryBean.getId() != null ? "Cập nhật thành công" : "Thêm thành công");
       

        return "redirect:/admin/list-category";
    }
}
