package com.ep.yunq.controller;

import com.ep.yunq.application.dto.AllStudentSignInCourseDTO;
import com.ep.yunq.application.dto.CourseSignInDTO;
import com.ep.yunq.application.dto.CourseStudentSignInDTO;
import com.ep.yunq.application.dto.StudentDTO;
import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.CourseSignIn;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.StudentSignIn;
import com.ep.yunq.domain.service.*;
import com.ep.yunq.infrastructure.util.CommonUtil;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @classname: SignInController
 * @Author: yan
 * @Date: 2021/4/26 10:22
 * 功能描述：
 **/
@Api(tags = "签到管理")
@Slf4j
@RestController
public class SignInController {

    @Autowired
    UserService userService;
    @Autowired
    CourseSignInService courseSignInService;
    @Autowired
    CourseService courseService;
    @Autowired
    StudentSignInService studentSignInService;
    @Autowired
    CourseToStudentService courseToStudentService;

    @ApiOperation("创建课程签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = "签到模式", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "value", value = "签到分钟数", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataTypeClass = BigDecimal.class),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataTypeClass = BigDecimal.class),
    })
    @PostMapping("/api/signIn")
    public Result<String> addCourseSignIn(@RequestParam String mode,@RequestParam Integer value,
                                          @RequestParam BigDecimal longitude,@RequestParam BigDecimal latitude,
                                          @RequestParam String code) {
        log.info("---------------- 创建课程签到 ----------------------");
        CourseSignIn courseSignIn=new CourseSignIn(mode,value,longitude,latitude);
        String message = courseSignInService.add(courseSignIn,code);
        if ("创建成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取课程的所有签到")
    @GetMapping("/api/signIn")
    public Result<Page<CourseSignInDTO>> getAllCourseSignInByCourse(@RequestParam String code,
                                                                 @RequestParam int pageNum,
                                                                 @RequestParam int pageSize) {
        log.info("---------------- 获取课程的所有签到 ----------------------");
        try {
            List<CourseSignIn> courseSignIns = courseSignInService.listAllByCourse(courseService.findByCode(code).getId());
            Page<CourseSignIn> courseSignInPage= PageUtil.listToPage(courseSignIns,pageNum,pageSize);
            Page<CourseSignInDTO> courseSignInDTOS= PageUtil.pageChange(courseSignInPage,CourseSignInDTO.class);
            return ResultUtil.buildSuccessResult(courseSignInDTOS);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数错误");
        }
    }

    @ApiOperation("获取课程的当前签到")
    @GetMapping("/api/signIn/courses")
    public Result<CourseSignInDTO> getCurrentCourseSignInByCourse(@RequestParam String code) {
        log.info("---------------- 获取课程的当前签到 ----------------------");
        try {
            CourseSignIn courseSignIn = courseSignInService.getCurrentSignInByCourseId(courseService.findByCode(code).getId());
            if(courseSignIn==null){
                return ResultUtil.buildFailResult("当前无签到");
            }
            ModelMapper modelMapper = new ModelMapper();
            CourseSignInDTO courseSignInDTO=modelMapper.map(courseSignIn,CourseSignInDTO.class);
            return ResultUtil.buildSuccessResult(courseSignInDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数错误");
        }

    }

    @ApiOperation("结束签到")
    @PutMapping("/api/signIn/end")
    public Result<String> endCourseSignIn(@RequestParam int courseSignInId) {
        log.info("---------------- 结束签到 ----------------------");
        String message= courseSignInService.endSignIn(courseSignInId);
        if ("结束签到".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }



    @ApiOperation("学生签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataTypeClass = BigDecimal.class),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataTypeClass = BigDecimal.class),
            @ApiImplicitParam(name = "courseSignId", value = "课程签到id", required = true, dataTypeClass = Integer.class),
    })
    @PostMapping("/api/signIn/students")
    public Result<String> signIn(@RequestParam Double distance,
                                 @RequestParam BigDecimal longitude,@RequestParam BigDecimal latitude,
                                 @RequestParam Integer courseSignId) {
        log.info("---------------- 学生签到 ----------------------");
        Integer uid= CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        StudentSignIn studentSignIn=new StudentSignIn( new Date(), longitude, latitude,uid,distance);
        String message= studentSignInService.add(studentSignIn,courseSignId);
        if ("签到成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else if ("请勿重复签到！".equals(message)) {
            return ResultUtil.buildResult(ConstantUtil.SIGNUP_REPEAT, message, null);
        }else if ("超出签到范围".equals(message)) {
            return ResultUtil.buildResult(ConstantUtil.SIGNUP_OUT_RANGE, message, null);
        }else if ("参数异常，签到失败".equals(message)) {
            return ResultUtil.buildFailResult(message);
        }else {
            return ResultUtil.buildResult(ConstantUtil.SIGNUP_VALUE_ERROR, message, null);
        }
    }

    @ApiOperation("获取学生某课程下的所有签到")
    @GetMapping("/api/signIn/students")
    public Result<List<CourseStudentSignInDTO>> getAllSignInByCourse(@RequestParam String code,@RequestParam int uid) {
        log.info("---------------- 学生所有签到 ----------------------");
        try {
            List<CourseStudentSignInDTO> maps = studentSignInService.getAllSignInByUserId(uid, courseService.findByCode(code).getId());
            return ResultUtil.buildSuccessResult(maps);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数错误");
        }

    }

    @ApiOperation("获取课程签到的学生")
    @GetMapping("/api/signIn/students/courses")
    public Result<Map<String,Object>> getAllSignInStudentByCourseSignIn(@RequestParam int courseSignInId) {
        log.info("---------------- 获取课程签到的学生 ----------------------");
        try {
            Map<String,Object> map= studentSignInService.getStudentInCourseSignIn(courseSignInId);
            return ResultUtil.buildSuccessResult(map);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数错误");
        }
    }
    
}
