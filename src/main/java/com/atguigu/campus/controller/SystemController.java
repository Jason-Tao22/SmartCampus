package com.atguigu.campus.controller;

import com.atguigu.campus.model.Admin;
import com.atguigu.campus.model.LoginForm;
import com.atguigu.campus.model.Student;
import com.atguigu.campus.model.Teacher;
import com.atguigu.campus.service.AdminService;
import com.atguigu.campus.service.StudentService;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@Api(tags = "System Controller")
@RestController
@RequestMapping("/sms/system")

public class SystemController {

    @Resource
    private AdminService adminService;
    @Resource
    private StudentService studentService;
    @Resource
    private TeacherService teacherService;


    /**
     * Generate the verify code and return to the web server.
     * */
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpSession session, HttpServletResponse response) throws IOException {

        BufferedImage verifyCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        String code = new String(CreateVerifiCodeImage.getVerifiCode());
        session.setAttribute("code",code);
        //return the image to the Web

        ImageIO.write(verifyCodeImage,"JPG",response.getOutputStream());
    }


    /**
     * response to the login button from users
     * */
    @PostMapping("/login")
    public Result<Object> login(HttpSession session, @RequestBody LoginForm loginForm){

        //verify the verified code;
        String verifycode = (String) session.getAttribute("code");
        if(verifycode == null||verifycode.equals("")){
            return Result.fail().message("verify code is overtime!");
        }

        if (loginForm.getVerifiCode() == null) {
            return Result.fail().message("Verification code is missing or null.");
        }
        
        if(!loginForm.getVerifiCode().equalsIgnoreCase(verifycode)) {
            return Result.fail().message("verify code is incorrect!");
        }

        //Verified stage is over, clean the verify code in cache;
        session.removeAttribute("code");

        //Check the username and password in the database;
        Integer usertype = loginForm.getUserType();
        String username = loginForm.getUsername();
        String password = MD5.encrypt(loginForm.getPassword());
        Map<String,Object> map = new LinkedHashMap<>();

        if(usertype==1){
            Admin admin = adminService.selectUserByUsernameAndPwd(username,password);
            if(admin != null){
                String token = JwtHelper.createToken(admin.getId().longValue(), usertype);
                map.put("token",token);
                return Result.ok(map);
            }else{
                return Result.fail().message("Wrong username or password!");
            }

        }else if(usertype==2){
            Student student = studentService.selectUserByNameAndPassword(username,password);
            if(student != null){
                String token = JwtHelper.createToken(student.getId().longValue(), usertype);
                map.put("token",token);
                return Result.ok(map);
            }else{
                return Result.fail().message("Wrong username or password!");
            }
        }else{
            Teacher teacher = teacherService.selectUserByNameAndPassword(username,password);
            if(teacher != null){
                String token = JwtHelper.createToken(teacher.getId().longValue(), usertype);
                map.put("token",token);
                return Result.ok(map);
            }else{
                return Result.fail().message("Wrong username or password!");
            }
        }

    }

    @GetMapping("/getInfo")
    public Result<Object> getInfo(@RequestHeader("token") String token){
        //verified whether the token overtime;
        if(JwtHelper.isExpiration(token)){

            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("userType",userType);

        if (userType==1){
            Admin admin = adminService.selectUserById(userId);
            map.put("user",admin);

        }else if(userType==2){
            Student student = studentService.selectUserById(userId);
            map.put("user",student);

        }else{
            Teacher teacher = teacherService.selectUserById(userId);
            map.put("user",teacher);
        }

        return Result.ok(map);
    }

    /**
     * aws files upload 
     * */
    @PostMapping("/headerImgUpload")
    public Result<Object> headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException{
        //First, get the name of uploaded file.
        String originalFilename = multipartFile.getOriginalFilename();
        assert  originalFilename != null;
        //get the format of files like: .jpg/.png/
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //using UUID to avoid the file because of the name is same
        String fileName = UUID.randomUUID().toString().toLowerCase().replace("-","").concat(suffix);

        String destPath = "D:/smart_campus-master/module_campus/src/main/resources/static/upload/".concat(fileName);

        multipartFile.transferTo(new File(destPath));

        return Result.ok("upload/".concat(fileName));

    }


    @PostMapping("/updatePwd/{OldPwd}/{NewPwd}")
    public  Result<Object> updatePwd(@PathVariable("OldPwd") String OldPwd,@PathVariable("NewPwd") String NewPwd,
                                     @RequestHeader("token") String token){

        //judge whether token is avaliable;
        if(JwtHelper.isExpiration(token)){
            return Result.build(null,ResultCodeEnum.TOKEN_ERROR);
        }

        OldPwd = MD5.encrypt(OldPwd);

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        assert userType != null;

        if(userType == 1){
            Admin admin = adminService.selectUserById(userId);
            if(!admin.getPassword().equals(OldPwd)){
                return Result.fail().message("Old Password is incorrect! Please input again!");
            }else{
                admin.setPassword(MD5.encrypt(NewPwd));
                adminService.update(admin,new LambdaQueryWrapper<Admin>().eq(Admin::getId,userId));

            }
        }else if(userType == 2){
            Student student = studentService.selectUserById(userId);
            if(!student.getPassword().equals(OldPwd)){
                return Result.fail().message("Old Password is incorrect! Please input again!");
            }else {
                student.setPassword(MD5.encrypt(NewPwd));
                studentService.update(student, new LambdaQueryWrapper<Student>().eq(Student::getId, userId));

            }
        }else{
            Teacher teacher = teacherService.selectUserById(userId);
            if(!teacher.getPassword().equals(OldPwd)){
                return Result.fail().message("Old Password is incorrect! Please input again!");
            }else {
                teacher.setPassword(MD5.encrypt(NewPwd));
                teacherService.update(teacher, new LambdaQueryWrapper<Teacher>().eq(Teacher::getId, userId));
            }

        }

        return Result.ok();

    }
}