package com.fpoly.java5.beans;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgotBean {
    @NotNull(message = "OTP không được bỏ trống")
    private Integer otp;
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String newpass;
    @NotBlank(message = "Xác nhận mật khẩu không được bỏ trống")
    private String cfpass;
}
