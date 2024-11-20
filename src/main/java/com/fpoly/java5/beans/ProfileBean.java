package com.fpoly.java5.beans;

import jakarta.validation.constraints.Email;
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
public class ProfileBean {
    private Integer id;
    @NotBlank(message = "Email không được bỏ trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
    @NotBlank(message = "Họ tên không được bỏ trống")
    @Pattern(regexp = "^[^\\d]+$", message = "Họ tên không được chứa ký tự số")
    private String name;
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số")
    private String phone;
    @NotBlank(message = "Địa chỉ không được bỏ trống")
    private String address;
    
}
