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
public class ResetPass {
    @NotBlank(message = "Mật khẩu cũ không được bỏ trống")
    private String oldpass;
    @NotBlank(message = "Mật khẩu mới không được bỏ trống")
    private String newpass;
    @NotBlank(message = "Xác nhận mật khẩu mới không được bỏ trống")
    private String cfpass;

}
