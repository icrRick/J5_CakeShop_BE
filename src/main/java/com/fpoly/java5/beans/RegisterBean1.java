package com.fpoly.java5.beans;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterBean1 {
    @NotBlank(message = "Email không được bỏ trống")
    private String email;
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
    @NotBlank(message = "Xác nhận mật khẩu không được bỏ trống")
    private String confirmpass;
    @NotBlank(message = "Tên tài khoản không được bỏ trống")
    private String username;
}
