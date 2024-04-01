package com.atguigu.campus.service;

import com.atguigu.campus.model.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface StudentService extends IService<Student> {

    Student selectUserByNameAndPassword(String username, String password);

    Student selectUserById(Long userId);
}
