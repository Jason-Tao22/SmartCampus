package com.atguigu.campus.controller;


import com.atguigu.campus.model.Clazz;
import com.atguigu.campus.model.Grade;
import com.atguigu.campus.model.Teacher;
import com.atguigu.campus.service.ClazzService;
import com.atguigu.campus.service.GradeService;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "Teacher Controller")
@RestController
@RequestMapping("/sms/teacherController")
public class teacherController {

    @Resource
    TeacherService teacherService;

    @ApiOperation("Selcet By Page and Pagesize")
    @GetMapping("/getTeachers/{pn}/{pageSize}")
    public Result<Object> getTeachers(@ApiParam("page now") @PathVariable Integer pn,
                                      @ApiParam("page size") @PathVariable Integer pageSize,
                                      @ApiParam("the teacher user provied") Teacher teacher){
        String clazzName = teacher.getClazzName();
        String name = teacher.getName();

        LambdaQueryWrapper<Teacher> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(clazzName),Teacher::getClazzName,clazzName).like(StrUtil.isNotBlank(name),Teacher::getName,name)
                .orderByDesc(Teacher::getId);

        Page<Teacher> page = teacherService.page(new Page<>(pn,pageSize),lambdaQueryWrapper);
        return Result.ok(page);
    }

    @ApiOperation("For other pages to select teachers")
    @GetMapping("/getTeachers")
    public Result<Object> getTeachers(){
        List<Teacher> teachers = teacherService.list(null);
        return Result.ok(teachers);
    }

    @DeleteMapping("/deleteTeacher")
    public Result<Object> deleteTeacher(@RequestBody List<Integer> mylist){
        if(mylist.size()==1){
            //delete single element
            teacherService.removeById(mylist.get(0));
        }else{
            teacherService.removeByIds(mylist);
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateTeacher")
    public Result<Object> saveOrUpdateTeacher(@RequestBody Teacher teacher){
        Integer id = teacher.getId();
        if(teacherService.getById(id) == null){
            teacherService.save(teacher);
        }else{
            teacherService.update(teacher,new LambdaQueryWrapper<Teacher>().eq(Teacher::getId,id));
        }

        return Result.ok();
    }







}
