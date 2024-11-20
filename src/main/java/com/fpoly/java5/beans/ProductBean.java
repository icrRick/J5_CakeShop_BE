package com.fpoly.java5.beans;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductBean {

    private Integer id;
    
    @NotBlank(message = "Tên sản phẩm không được để trống.")
    @Pattern(regexp = "^[^\\d]+$", message = "Tên sản phẩm không được chứa ký tự số")
    private String name;
    @NotBlank(message = "Hương vị không được để trống.")
    @Pattern(regexp = "^[^\\d]+$", message = "Hương vị không được chứa ký tự số")
    private String flavor;
    @NotBlank(message = "Thành phần không được để trống.")
    @Pattern(regexp = "^[^\\d]+$", message = "Thành phần không được chứa ký tự số")
    private String ingredients;
    @NotBlank(message = "Mô tả không được để trống.")
    private String descriptions;

    @NotNull(message = "Số lượng không được để trống.")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1.")
    private Integer quantity=1;

    @NotNull(message = "Giá không được để trống.")
    @Min(value = 10000, message = "Giá phải lớn hơn hoặc bằng 10000.")
    private Double price=10000.0;

    private Boolean active=true;

    private Integer categoryId;

    private ArrayList<MultipartFile> images =new ArrayList<>();
}
