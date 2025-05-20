package com.tienhuynh.auth_service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Email(message = "Email không hợp lệ")
    public String mail;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Length(min = 8, max = 20,message = "Mật khẩu phải có ít nhất 8 ký tự")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@]+$",
            message = "Mật khẩu phải chứa ít nhất 1 chữ in hoa, 1 chữ thường, 1 số và không chứa ký tự đặc biệt"
    )
    public String pwd;

    @NotEmpty(message = "Họ tên không được để trống")
    public String fullName;

    public String phone;

    public String role;

    public Map<String,Object> profile;
}