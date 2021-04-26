package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.CourseSignIn;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.StudentSignIn;
import com.ep.yunq.domain.service.CourseSignInService;
import com.ep.yunq.domain.service.StudentSignInService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    StudentSignInService studentSignInService;

    @PostMapping("/api/class/signIn/add")
    public Result addCourseSignIn(@RequestBody CourseSignIn courseSignIn) {
        log.info("---------------- 创建课程签到 ----------------------");
        String message = courseSignInService.add(courseSignIn);
        if ("创建成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @GetMapping("/api/class/signIn/all")
    public Result getAllCourseSignInByCourse(@RequestParam int cid) {
        log.info("---------------- 获取课程的所有签到 ----------------------");
        List<CourseSignIn> courseSignIns = courseSignInService.listAllByCourse(cid);
        return ResultUtil.buildSuccessResult(courseSignIns);
    }

    @GetMapping("/api/class/stu/signIn/now")
    public Result getCurrentCourseSignInByCourse(@RequestParam int cid) {
        log.info("---------------- 获取课程的当前签到 ----------------------");
        CourseSignIn courseSignIn = courseSignInService.getCurrentSignInByCourseId(cid);
        return ResultUtil.buildSuccessResult(courseSignIn);
    }

    @GetMapping("/api/class/signIn/end")
    public Result endCourseSignIn(@RequestParam int csuid) {
        log.info("---------------- 结束签到 ----------------------");
        String message= courseSignInService.endSignIn(csuid);
        if ("结束签到".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }



    @PostMapping("/api/class/stu/signIn")
    public Result signIn(@RequestBody StudentSignIn studentSignIn) {
        log.info("---------------- 学生签到 ----------------------");
        String message= studentSignInService.add(studentSignIn);
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

    @GetMapping("/api/class/stu/signIn/all")
    public Result getAllSignInByCourse(@RequestParam int cid,@RequestParam int uid) {
        log.info("---------------- 学生所有签到 ----------------------");
        List<Map<String, Object>> maps = studentSignInService.getAllSignInByUserId(uid, cid);
        return ResultUtil.buildSuccessResult(maps);
    }

    @GetMapping("/api/class/signIn/stu")
    public Result getAllSignInStudentByCourseSignIn(@RequestParam int csiid) {
        log.info("---------------- 课程签到的学生 ----------------------");
        List<Map<String, Object>> maps = studentSignInService.getAllSignInByCourseSignIn(csiid);
        return ResultUtil.buildSuccessResult(maps);
    }
    
}
