package com.laclippers.los.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UpdateProfileRequest {
    @Size(min = 2, max = 20, message = "用户名长度需在 2-20 之间")
    private String username;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String avatar;

    @Size(max = 200, message = "个人简介长度不能超过 200 字")
    private String bio;
}