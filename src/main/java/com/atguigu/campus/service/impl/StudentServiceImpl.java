package com.atguigu.campus.service.impl;

import com.atguigu.campus.mapper.AdminMapper;
import com.atguigu.campus.model.Admin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.model.Student;
import com.atguigu.campus.service.StudentService;
import com.atguigu.campus.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{
    @Resource StudentMapper studentMapper;

    @Override
    public Student selectUserByNameAndPassword(String username, String password) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getName,username).eq(Student::getPassword,password));
    }

    @Override
    public Student selectUserById(Long userId) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId,userId));
    }
}




