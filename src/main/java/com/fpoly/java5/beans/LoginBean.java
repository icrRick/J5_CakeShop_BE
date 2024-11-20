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
public class LoginBean {
    @NotBlank(message = "Username không được bỏ trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
}
