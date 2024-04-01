package com.atguigu.campus.controller;

import com.atguigu.campus.model.Admin;
import com.atguigu.campus.model.Grade;
import com.atguigu.campus.service.AdminService;
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


@Api(tags = "Admin Controller")
@RestController
@RequestMapping("/sms/adminController")
public class adminController {
    @Resource
    AdminService adminService;

    @ApiOperation("Selcet By Page and Pagesize")
    @GetMapping("/getAllAdmin/{pn}/{pageSize}")
    public Result<Object> getAllAdmin(@ApiParam("page now") @PathVariable Integer pn,
                                    @ApiParam("page size") @PathVariable Integer pageSize,
                                    @ApiParam("Admin name provided") String adminName){

        Page<Admin> page = adminService.page(new Page<Admin>(pn,pageSize),
                new LambdaQueryWrapper<Admin>().like(StrUtil.isNotBlank(adminName),Admin::getName,adminName).orderByDesc(Admin::getId));

        return Result.ok(page);
    }

    @ApiOperation("For other pages to select admin")
    @GetMapping("/getAdmins")
    public Result<Object> getAdmins(){
        List<Admin> admins = adminService.list(null);
        return Result.ok(admins);
    }




    @DeleteMapping("/deleteAdmin")
    public Result<Object> deleteAdmin(@RequestBody List<Integer> mylist){
        if(mylist.size()==1){
            //delete single element
            adminService.removeById(mylist.get(0));
        }else{
            adminService.removeByIds(mylist);
        }
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result<Object> saveOrUpdateAdmin(@RequestBody Admin admin){
        Integer id = admin.getId();
        if(adminService.getById(id) == null){
            adminService.save(admin);
        }else{
            adminService.update(admin,new LambdaQueryWrapper<Admin>().eq(Admin::getId,id));
        }

        return Result.ok();
    }
}
