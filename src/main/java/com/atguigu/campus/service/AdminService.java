package com.atguigu.campus.service;

import com.atguigu.campus.model.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface AdminService extends IService<Admin> {

    Admin selectUserByUsernameAndPwd(String username, String password);

    Admin selectUserById(Long userId);
}
