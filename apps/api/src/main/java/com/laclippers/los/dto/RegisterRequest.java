package com.laclippers.los.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotBlank(message = "请输入用户名")
    @Size(min = 2, max = 20, message = "用户名长度需在 2-20 之间")
    private String username;

    @NotBlank(message = "请输入邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "请输入密码")
    @Size(min = 6, message = "密码长度至少 6 位")
    private String password;
}