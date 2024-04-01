package com.atguigu.campus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginForm {

    private String username;

    private String password;

    private String verifiCode;

    private Integer userType;
}



