package com.fpoly.java5.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.java5.beans.ProductBean;
import com.fpoly.java5.entities.Category;
import com.fpoly.java5.entities.Image;
import com.fpoly.java5.entities.Product;
import com.fpoly.java5.jpa.CategoryJPA;
import com.fpoly.java5.jpa.ImageJPA;
import com.fpoly.java5.jpa.ProductJPA;

import jakarta.validation.Valid;

@Controller
public class ManageProductController {
    @Autowired
    private ProductJPA productJPA;
    @Autowired
    private ImageJPA imageJPA;
    @Autowired
    private CategoryJPA categoryJPA;

    @GetMapping("/admin/list-product")
    public String listProductPage(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("pageTitle", "Danh sách loại sản phẩm");
        Pageable pageable = PageRequest.of(page - 1, 10);
        String returnUrl = "/admin/list-product?page=" + page; // Thay đổi tên biến
        model.addAttribute("returnUrl", returnUrl);
        Page<Product> getPageAllProduct = productJPA.pageAllProduct(pageable);
        model.addAttribute("getPageAllProduct", getPageAllProduct);
        model.addAttribute("viewPage", "fragment/product/pagi_product");
        return "admin/list-product";
    }

    @GetMapping("/admin/filter-product")
    public String filterUser(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Boolean status) {
        model.addAttribute("pageTitle", "Danh sách người dùng");
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<Product> getPageAllProduct = productJPA.pageAllFilterProduct(keyword, status, pageable);
        if (keyword.equals("") && status == null) {
            return "redirect:/admin/list-product";
        }
        model.addAttribute("getPageAllProduct", getPageAllProduct);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("status", status);
        model.addAttribute("returnUrl", "/admin/filter-product?keyword=" + (keyword != null ? keyword : "")
                + "&status?=" + status + "&page" + page);
        model.addAttribute("viewPage", "fragment/product/pagi_filterproduct");

        return "admin/list-product";
    }

    @PostMapping("/admin/remove-product")
    public String removeCategory(Model model, @RequestParam("returnUrl") String returnUrl,
            @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        Optional<Product> product = productJPA.findById(id);
        if (product.isPresent()) {
            product.get().setActive(false);
            productJPA.save(product.get());
            redirectAttributes.addFlashAttribute("message", "Xóa thành công");
        }
        return "redirect:" + returnUrl;

    }

    @GetMapping("/admin/form-product")
    public String fromProductPage(Model model, @RequestParam("id") Optional<Integer> id) {
        model.addAttribute("pageTitle", "Form loại sản phẩm");
        if (id.isPresent()) {
            Optional<Product> product = productJPA.findById(id.get());
            if (product.isPresent()) {
                Product p = product.get();
                ProductBean productBean = new ProductBean();
                productBean.setId(p.getId());
                productBean.setName(p.getName());
                productBean.setFlavor(p.getFlavor());
                productBean.setIngredients(p.getIngredients());
                productBean.setDescriptions(p.getDescriptions());
                productBean.setQuantity(p.getQuantity());
                productBean.setPrice(p.getPrice());
                productBean.setActive(p.getActive());
                productBean.setCategoryId(p.getCategoryId());
                model.addAttribute("productBean", productBean);
            } else {
                return "error/404";
            }
        } else {
            model.addAttribute("productBean", new ProductBean());
        }
        return "admin/form-product";
    }

    @PostMapping("/admin/form-product")
    public String saveCategory(
            Model model,
            @Valid @ModelAttribute("productBean") ProductBean productBean,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("pageTitle", "Form loại sản phẩm");
        if (productBean.getId() != null) {
            Optional<Product> opProductCheckImage = productJPA.findById(productBean.getId());
            if (opProductCheckImage.isPresent()) {
                if (!opProductCheckImage.get().getImages().isEmpty()) {
                } else {
                    if (productBean.getImages() == null
                            || productBean.getImages().stream().allMatch(MultipartFile::isEmpty)) {
                        model.addAttribute("errimages", "Vui lòng chọn hình");
                        return "admin/form-product";

                    }
                }
            } else {
                if (productBean.getImages() == null
                        || productBean.getImages().stream().allMatch(MultipartFile::isEmpty)) {
                    model.addAttribute("errimages", "Vui lòng chọn hình");
                    return "admin/form-product";

                }
            }
        } else {
            if (productBean.getImages() == null || productBean.getImages().stream().allMatch(MultipartFile::isEmpty)) {
                model.addAttribute("errimages", "Vui lòng chọn hình");
                return "admin/form-product";

            }
        }

      
        if (!productBean.getName().isBlank()) {
            Optional<Product> opProduct = productJPA.findByNameExist(productBean.getId(), productBean.getName());
            if (opProduct.isPresent()) {
                result.rejectValue("name", "error.productBean", "Tên sản phẩm đã tồn tại");
                model.addAttribute("productBean", productBean);
                return "admin/form-product";
            }
        }
        if (result.hasErrors()) {
            return "admin/form-product";
        }
        Product product = new Product();
        Optional<Category> category = categoryJPA.findById(productBean.getCategoryId());
        if (category.isPresent()) {
            if (productBean.getId() != null) {
                Optional<Product> optionalProduct = productJPA.findById(productBean.getId());
                if (optionalProduct.isPresent()) {
                    product = optionalProduct.get();
                    product.setActive(productBean.getActive());
                }
            }

            product.setName(productBean.getName());
            product.setFlavor(productBean.getFlavor());
            product.setIngredients(productBean.getIngredients());
            product.setQuantity(productBean.getQuantity());
            product.setPrice(productBean.getPrice());
            product.setDescriptions(productBean.getDescriptions());
            product.setCategory(category.get());
            product.setActive(productBean.getActive());

            Product saveProduct = productJPA.save(product);
            for (MultipartFile file : productBean.getImages()) {
                if (!file.isEmpty()) {
                    try {
                        String fileName = System.currentTimeMillis() + ".jpg";
                        Path filePath = Paths.get("images/" + fileName);
                        Files.copy(file.getInputStream(), filePath);
                        Image image = new Image();
                        image.setName(fileName);
                        image.setProduct(saveProduct);
                        imageJPA.save(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("message",
                productBean.getId() != null ? "Cập nhật thành công" : "Thêm thành công");

        return "redirect:/admin/list-product";
    }

}
