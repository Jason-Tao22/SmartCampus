package com.atguigu.campus.service;

import com.atguigu.campus.model.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface TeacherService extends IService<Teacher> {

    Teacher selectUserByNameAndPassword(String username, String password);

    Teacher selectUserById(Long userId);
}
