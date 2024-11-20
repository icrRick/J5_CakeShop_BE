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
public class RegisterBean2 {
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phone;
    @NotBlank(message = "Họ tên không được bỏ trống")
     @Pattern(regexp = "^[^\\d]+$", message = "Họ tên không được chứa ký tự số")
    private String name;
    @NotBlank(message = "Địa chỉ không được bỏ trống")
    private String address;
 
}
