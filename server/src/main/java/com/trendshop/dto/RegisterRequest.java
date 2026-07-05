package com.trendshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "用户名长度需要3-20位")
    @Size(min = 3, max = 20, message = "用户名长度需要3-20位")
    private String username;

    @NotBlank(message = "密码长度需要6-32位")
    @Size(min = 6, max = 32, message = "密码长度需要6-32位")
    private String password;

    @NotBlank(message = "邮箱格式不正确")
    @Email(message = "邮箱格式不正确")
    private String email;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
