package com.atguigu.campus.service.impl;

import com.atguigu.campus.model.Admin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.model.Teacher;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{
    @Resource TeacherMapper teacherMapper;

    @Override
    public Teacher selectUserByNameAndPassword(String username, String password) {
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getName,username).eq(Teacher::getPassword,password));
    }

    @Override
    public Teacher selectUserById(Long userId) {
        return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,userId));
    }
}




