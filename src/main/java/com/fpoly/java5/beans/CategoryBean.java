package com.fpoly.java5.beans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryBean {
    private Integer id ;
    @NotBlank(message = "Tên loại sản phẩm không được bỏ trống")
    @Pattern(regexp = "^[^\\d]+$", message = "Tên loại sản phẩm không được chứa ký tự số")
    private String name;
    private Boolean active=true;
}
