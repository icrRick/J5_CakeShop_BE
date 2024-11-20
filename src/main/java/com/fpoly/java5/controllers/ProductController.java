package com.fpoly.java5.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.fpoly.java5.entities.Product;
import com.fpoly.java5.jpa.ProductJPA;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @Autowired
    private ProductJPA g_ProductJPA;

    @GetMapping("/products")
    public String getProductPage(Model model, @RequestParam(defaultValue = "1") int page) {
        model.addAttribute("pageTitle", "Sản phẩm");
        model.addAttribute("namePage", "Sản phẩm");
        Pageable pageable = PageRequest.of(page - 1, 9);
        Page<Product> getAllPageProduct = g_ProductJPA.getPageAllProductOrderById(pageable);
        String currentUrl = "/products?page="+page;
        model.addAttribute("currentUrl", currentUrl);
        model.addAttribute("getAllPageProduct", getAllPageProduct);
        model.addAttribute("viewPage", "fragment/pagination/product/pagi_products");
        return "views/product";
    }

    @GetMapping("/products/filter")
public String getFilterProduct(Model model,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice,
                               @RequestParam(defaultValue = "asc") String sortBy,
                               @RequestParam(defaultValue = "1") int page) {

    model.addAttribute("pageTitle", "Sản phẩm");
    model.addAttribute("namePage", "Sản phẩm");

    // Ensure minPrice and maxPrice are parsed correctly
    Double actualMinPrice = (minPrice != null) ? minPrice : 0.0;
    Double actualMaxPrice = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;

    // Create Sort object based on sortBy parameter
    Sort sort = "desc".equalsIgnoreCase(sortBy) ? Sort.by("price").descending() : Sort.by("price").ascending();

    // Create pageable with sort
    Pageable pageable = PageRequest.of(page - 1, 9, sort);

    // Log the input prices
    System.out.println("Min Price: " + actualMinPrice);
    System.out.println("Max Price: " + actualMaxPrice);

    // Fetch filtered products
    Page<Product> filteredProducts = g_ProductJPA.getFilterProductByActive(keyword, categoryId, actualMinPrice, actualMaxPrice, pageable);
    model.addAttribute("getAllPageProduct", filteredProducts);
    model.addAttribute("viewPage", "fragment/pagination/product/pagi_filterproducts");

    // Recalculate the actual min and max prices from the filtered products
    double calculatedMinPrice = Double.MAX_VALUE;
    double calculatedMaxPrice = Double.MIN_VALUE;

    if (!filteredProducts.getContent().isEmpty()) {
        for (Product product : filteredProducts.getContent()) {
            double price = product.getPrice();
            if (price < calculatedMinPrice) {
                calculatedMinPrice = price;
            }
            if (price > calculatedMaxPrice) {
                calculatedMaxPrice = price;
            }
        }
    }

    // Handle case where no products are found
    if (calculatedMinPrice == Double.MAX_VALUE) calculatedMinPrice = 0.0;
    if (calculatedMaxPrice == Double.MIN_VALUE) calculatedMaxPrice = 0.0;

    // Log the recalculated prices
    System.out.println("Calculated Min Price: " + calculatedMinPrice);
    System.out.println("Calculated Max Price: " + calculatedMaxPrice);

    // Add attributes to the model
    model.addAttribute("actualMinPrice", calculatedMinPrice);
    model.addAttribute("actualMaxPrice", calculatedMaxPrice);
    model.addAttribute("minPrice", calculatedMinPrice);
    model.addAttribute("maxPrice", calculatedMaxPrice);
    model.addAttribute("keyword", keyword);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("currentUrl", "/products/filter?keyword=" + keyword + "&categoryId=" + categoryId + "&minPrice=" + actualMinPrice + "&maxPrice=" + actualMaxPrice + "&sortBy=" + sortBy + "&page=" + page);

    return "views/product";  
}

    

}
