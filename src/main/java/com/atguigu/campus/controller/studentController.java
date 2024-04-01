package com.atguigu.campus.controller;

import com.atguigu.campus.model.Student;
import com.atguigu.campus.service.StudentService;
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

@Api(tags = "Student Controller")
@RestController
@RequestMapping("/sms/studentController")

public class studentController {
    @Resource
    StudentService studentService;

    @ApiOperation("Selcet By Page and Pagesize")
    @GetMapping("/getStudentByOpr/{pn}/{pageSize}")
    public Result<Object> getStudent(@ApiParam("page now") @PathVariable Integer pn,
                                      @ApiParam("page size") @PathVariable Integer pageSize,
                                      @ApiParam("the student privided") Student student){
        String clazzName = student.getClazzName();
        String name = student.getName();

        LambdaQueryWrapper<Student> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(clazzName),Student::getClazzName,clazzName).like(StrUtil.isNotBlank(name),Student::getName,name)
                .orderByDesc(Student::getId);

        Page<Student> page = studentService.page(new Page<>(pn,pageSize),lambdaQueryWrapper);
        return Result.ok(page);
    }

    @ApiOperation("For other pages to select students")
    @GetMapping("/getStudent")
    public Result<Object> getStudents(){
        List<Student> students = studentService.list(null);
        return Result.ok(students);
    }

    @DeleteMapping("/deleteStudent")
    public Result<Object> deleteStudent(@RequestBody List<Integer> mylist){
        if(mylist.size()==1){
            //delete single element
            studentService.removeById(mylist.get(0));
        }else{
            studentService.removeByIds(mylist);
        }
        return Result.ok();
    }

    @PostMapping("/addOrUpdateStudent")
    public Result<Object> addOrUpdateStudent(@RequestBody Student student){
        Integer id = student.getId();
        if(studentService.getById(id) == null){
            studentService.save(student);
        }else{
            studentService.update(student,new LambdaQueryWrapper<Student>().eq(Student::getId,id));
        }

        return Result.ok();
    }


}
