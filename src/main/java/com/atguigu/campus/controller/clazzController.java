package com.atguigu.campus.controller;


import com.atguigu.campus.model.Clazz;
import com.atguigu.campus.model.Grade;
import com.atguigu.campus.service.ClazzService;
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

@Api(tags = "Class Controller")
@RestController
@RequestMapping("/sms/clazzController")
public class clazzController {

    @Resource
    ClazzService clazzService;

    @ApiOperation("Selcet By Page and Pagesize")
    @GetMapping("/getClazzsByOpr/{pn}/{pageSize}")
    public Result<Object> getClazzsByOpr(@ApiParam("page now") @PathVariable Integer pn,
                                         @ApiParam("page size") @PathVariable Integer pageSize,
                                         @ApiParam("the class user provied") Clazz clazz){

        String className = clazz.getName();
        String gradeName = clazz.getGradeName();


        LambdaQueryWrapper<Clazz> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(className),Clazz::getName,className).like(StrUtil.isNotBlank(gradeName),Clazz::getGradeName,gradeName)
                .orderByDesc(Clazz::getId);

        Page<Clazz> page = clazzService.page(new Page<>(pn,pageSize),lambdaQueryWrapper);
        return Result.ok(page);
    }

    @ApiOperation("For other pages to select classes")
    @GetMapping("/getClazzs")
    public Result<Object> getClazzs(){
        List<Clazz> clazzes = clazzService.list(null);
        return Result.ok(clazzes);
    }

    @ApiOperation("Remove Classes")
    @DeleteMapping("/deleteClazzs")
    public Result<Object> deleteClazz(@RequestBody List<Integer> mylist){
        if(mylist.size()==1){
            //delete single element
            clazzService.removeById(mylist.get(0));
        }else{
            clazzService.removeByIds(mylist);
        }
        return Result.ok();
    }

    @ApiOperation("Modify Class Info")
    @PostMapping("/saveOrUpdateClazz")
    public Result<Object> saveOrUpdateClazz(@RequestBody Clazz clazz){
        Integer id = clazz.getId();
        if(clazzService.getById(id) == null){
            clazzService.save(clazz);
        }else{
            clazzService.update(clazz,new LambdaQueryWrapper<Clazz>().eq(Clazz::getId,id));
        }

        return Result.ok();
    }



}
