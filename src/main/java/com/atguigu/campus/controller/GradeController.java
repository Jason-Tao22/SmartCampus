package com.atguigu.campus.controller;


import com.atguigu.campus.model.Grade;
import com.atguigu.campus.service.GradeService;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;


@Api(tags = "Grade Controller")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {
    @Resource
    GradeService gradeService;

    @ApiOperation("Selcet By Page and Pagesize")
    @GetMapping("/getGrades/{pn}/{pageSize}")
    public Result<Object> getGrades(@ApiParam("page now") @PathVariable Integer pn,
                                    @ApiParam("page size") @PathVariable Integer pageSize,
                                    @ApiParam("The name users provided") String gradeName){

        Page<Grade> page = gradeService.page(new Page<Grade>(pn,pageSize),
                new LambdaQueryWrapper<Grade>().like(StrUtil.isNotBlank(gradeName),Grade::getName,gradeName).orderByDesc(Grade::getId));

        return Result.ok(page);
    }

    @ApiOperation("For other pages to select grades")
    @GetMapping("/getGrades")
    public Result<Object> getGrades(){
        List<Grade> grades = gradeService.list(null);
        return Result.ok(grades);
    }



    @DeleteMapping("/deleteGrade")
    public Result<Object> deleteGrade(@RequestBody List<Integer> mylist){
        if(mylist.size()==1){
            //delete single element
            gradeService.removeById(mylist.get(0));
        }else{
            gradeService.removeByIds(mylist);
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateGrade")
    public Result<Object> saveOrUpdateGrade(@RequestBody Grade grade){
        Integer id = grade.getId();
        if(gradeService.getById(id) == null){
            gradeService.save(grade);
        }else{
            gradeService.update(grade,new LambdaQueryWrapper<Grade>().eq(Grade::getId,id));
        }

        return Result.ok();
    }


}
