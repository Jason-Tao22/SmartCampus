package com.atguigu.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.model.Admin;
import com.atguigu.campus.service.AdminService;
import com.atguigu.campus.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service

public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService{

    @Resource AdminMapper adminMapper;


    @Override
    public Admin selectUserByUsernameAndPwd(String username, String password) {
        return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getName,username).eq(Admin::getPassword,password));
    }

    @Override
    public Admin selectUserById(Long userId) {
        return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId,userId));
    }


}




