package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.CourseService;
import com.ep.yunq.domain.service.CourseToStudentService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @classname: CourseController
 * @Author: yan
 * @Date: 2021/4/13 10:05
 * 功能描述：
 **/

@Api(tags = "课程管理")
@Slf4j
@RestController
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseToStudentService courseToStudentService;
    @Autowired
    UserService userService;

    /** -------------------------- 课程表 -------------------------------------- **/

    @ApiOperation("添加课程")
    @PostMapping("/api/class/course/add")
    public Result addCourse(@RequestBody Course params) {
        log.info("---------------- 添加课程 ----------------------");

        String message="";
        Course course = new Course();
        course.setName(params.getName());
        course.setGrade(params.getGrade());
        course.setSemester(params.getSemester());
        course.setSchool(params.getSchool());
        course.setCollege(params.getCollege());
        course.setMajor(params.getMajor());
        course.setTeacher(params.getTeacher());
        course.setLearnRequire(params.getLearnRequire());
        course.setTeachProgress(params.getTeachProgress());
        course.setExamArrange(params.getExamArrange());
        course.setCover(params.getCover());
        /*course.setSchoolId(Integer.parseInt(params.getParameter("schoolId")));
        course.setCollegeId(Integer.parseInt(params.getParameter("collegeId")));*/
        log.info(params.getCover());
        message = courseService.add(course);
        if ("参数异常，添加失败".equals(message))
            return ResultUtil.buildFailResult(message);
        else
            return ResultUtil.buildSuccessResult(message);
    }

    @ApiOperation("修改课程包含头像")
    @PostMapping("/api/class/course/modify")
    public Result editCourseContainCover(HttpServletRequest request) {
        log.info("---------------- 修改课程包含头像 ----------------------");
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("cover");
        Course course = new Course();
        course.setId(Integer.parseInt(params.getParameter("id")));
        course.setName(params.getParameter("name"));
        course.setGrade(params.getParameter("grade"));
        course.setSemester(params.getParameter("semester"));
        course.setSchool(params.getParameter("school"));
        course.setCollege(params.getParameter("college"));
        course.setMajor(params.getParameter("major"));
        course.setTeacher(params.getParameter("teacher"));
        course.setLearnRequire(params.getParameter("learnRequire"));
        course.setTeachProgress(params.getParameter("teachProgress"));
        course.setExamArrange(params.getParameter("examArrange"));
        String message = "";
        if(0 == files.size()) {
            message = courseService.editContainCover(course, null);
        } else {
            message = courseService.editContainCover(course, files.get(0));
        }
        if ("参数异常，修改失败".equals(message))
            return ResultUtil.buildFailResult(message);
        else
            return ResultUtil.buildSuccessResult(message);
    }

    @ApiOperation("删除课程")
    @GetMapping("/api/class/course/delete")
    public Result deleteCourse(@RequestParam int cid) {
        log.info("---------------- 删除课程 ----------------------");
        String message = courseService.delete(cid);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("批量删除课程")
    @PostMapping("/api/class/course/delete")
    public Result batchDeleteCourse(@RequestBody LinkedHashMap courseIds) {
        log.info("---------------- 批量删除课程 ----------------------");
        String message = courseService.batchDelete(courseIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改课程")
    @PutMapping("/api/class/course/edit")
    public Result eidtCourse(@RequestBody Course course) {
        log.info("---------------- 修改课程 ----------------------");
        String message = courseService.edit(course);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改课程封面")
    @PostMapping("/api/class/course/cover")
    public Result updateCourseCover(HttpServletRequest request) throws Exception {
        log.info("---------------- 修改课程封面 ----------------------");
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        int cid = Integer.parseInt(params.getParameter("id"));
        String message = courseService.updateCover(files.get(0), cid);
        if (!"更新失败".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我创建的课程")
    @GetMapping("/api/class/course/getCreate")
    public Result getCurrentUserCreateCourse(@RequestParam String phone) {
        log.info("---------------- 获取我创建的课程 ----------------------");
        List<Course> courses= courseService.findAllByCreatorId(userService.findByPhone(phone).getId());
        return ResultUtil.buildSuccessResult(courses);
    }

    @ApiOperation("搜索课程")
    @GetMapping("/api/class/course/search")
    public Result searchCourse(@RequestParam String keywords) {
        log.info("---------------- 搜索课程 ----------------------");
        List<Course> courses= courseService.search(keywords);
        return ResultUtil.buildSuccessResult(courses);
    }

    @ApiOperation("获取所有课程")
    @GetMapping("/api/class/course/all")
    public Result getAllCourse() {
        log.info("---------------- 获取所有课程 ----------------------");
        List<Course> courses= courseService.findAll();
        return ResultUtil.buildSuccessResult(courses);
    }

    @ApiOperation("获取单一课程")
    @GetMapping("/api/class/stu/course/get/{cid}")
    public Result getById(@PathVariable("cid") int cid) {
        log.info("---------------- 获取单一课程 ----------------------");
        Course course= courseService.findById(cid);
        if (null != course)
            return ResultUtil.buildSuccessResult(course);
        else
            return ResultUtil.buildFailResult("请输入正确课程Id");
    }

    /** -------------------------- 课程学生表 -------------------------------------- **/


    @ApiOperation("获取课程学生")
    @GetMapping("/api/class/stu/course/stu")
    public Result getStudentsByCourseId(@RequestParam int cid) {
        log.info("---------------- 获取课程学生 ----------------------");
        List<Map<String,String>> courseStus = courseToStudentService.findAllStudentByCourseId(cid);
        return ResultUtil.buildSuccessResult(courseStus);
    }

    @ApiOperation("加入课程")
    @GetMapping("/api/class/stu/course/join")
    public Result joinCourse(@RequestParam int cid,@RequestParam String phone) {
        log.info("---------------- 加入课程 ----------------------");
        String message = courseToStudentService.joinCourse(userService.findByPhone(phone).getId(), cid);
        if ("加入成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我加入的课程")
    @GetMapping("/api/class/stu/course/getJoin")
    public Result getCurrentUserJoinCourse(@RequestParam String phone) {
        log.info("---------------- 获取我加入的课程 ----------------------");
        List<Course> courses= courseToStudentService.findCourseByUserId(userService.findByPhone(phone).getId());
        return ResultUtil.buildSuccessResult(courses);
    }
}
