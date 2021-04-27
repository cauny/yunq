package com.ep.yunq.controller;

import com.ep.yunq.application.dto.CourseDTO;
import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.CourseService;
import com.ep.yunq.domain.service.CourseToStudentService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    /*@ApiOperation("添加课程")
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
        *//*course.setSchoolId(Integer.parseInt(params.getParameter("schoolId")));
        course.setCollegeId(Integer.parseInt(params.getParameter("collegeId")));*//*
        log.info(params.getCover());
        message = courseService.add(course);
        if ("参数异常，添加失败".equals(message))
            return ResultUtil.buildFailResult(message);
        else
            return ResultUtil.buildSuccessResult(message);
    }*/


    @ApiOperation("添加课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "课程名字",required=true, dataType = "string"),
            @ApiImplicitParam(name = "grade", value = "年级",required=true, dataType = "string"),
            @ApiImplicitParam(name = "semester", value = "年级",required=true, dataType = "string"),
            @ApiImplicitParam(name = "school", value = "学校",required=true, dataType = "string"),
            @ApiImplicitParam(name = "college", value = "院系",required=true, dataType = "string"),
            @ApiImplicitParam(name = "major", value = "专业",required=true, dataType = "string"),
            @ApiImplicitParam(name = "teacher", value = "教师",required=true, dataType = "string"),
            @ApiImplicitParam(name = "learnRequire", value = "学习要求",required=false, dataType = "string"),
            @ApiImplicitParam(name = "teachProgress", value = "教学进度",required=false, dataType = "string"),
            @ApiImplicitParam(name = "examArrange", value = "考试安排",required=false, dataType = "string"),
            @ApiImplicitParam(name = "cover", value = "封面",required=true, dataType = "string"),
            @ApiImplicitParam(name = "creator", value = "创建者手机号",required=true, dataType = "string"),
    })
    @ApiResponse(code = 200,message = "成功，data返回null" )
    @PostMapping("/api/class/courses")
    public Result addCourse(HttpServletRequest request) {
        log.info("---------------- 添加课程 ----------------------");
        String message="";
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("cover");
        Course course = new Course();
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
        course.setCreator(userService.findByPhone(params.getParameter("creator")).getId());
        /*course.setSchoolId(Integer.parseInt(params.getParameter("schoolId")));
        course.setCollegeId(Integer.parseInt(params.getParameter("collegeId")));*/

        message = courseService.add(course, files.get(0));
        log.info(String.valueOf(files.get(0)));
        if ("参数异常，添加失败".equals(message))
            return ResultUtil.buildFailResult(message);
        else
            return ResultUtil.buildSuccessResult(message,null);
    }

    @ApiOperation("修改课程包含头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id",required=true, dataType = "string"),
            @ApiImplicitParam(name = "name", value = "课程名字",required=true, dataType = "string"),
            @ApiImplicitParam(name = "grade", value = "年级",required=true, dataType = "string"),
            @ApiImplicitParam(name = "semester", value = "年级",required=true, dataType = "string"),
            @ApiImplicitParam(name = "school", value = "学校",required=true, dataType = "string"),
            @ApiImplicitParam(name = "college", value = "院系",required=true, dataType = "string"),
            @ApiImplicitParam(name = "major", value = "专业",required=true, dataType = "string"),
            @ApiImplicitParam(name = "teacher", value = "教师",required=true, dataType = "string"),
            @ApiImplicitParam(name = "learnRequire", value = "学习要求",required=false, dataType = "string"),
            @ApiImplicitParam(name = "teachProgress", value = "教学进度",required=false, dataType = "string"),
            @ApiImplicitParam(name = "examArrange", value = "考试安排",required=false, dataType = "string"),
            @ApiImplicitParam(name = "cover", value = "封面",required=true, dataType = "string"),
            @ApiImplicitParam(name = "modifier", value = "修改者手机号",required=true, dataType = "string"),
    })
    @ApiResponse(code = 200,message = "修改成功，data返回null" )
    @PutMapping("/api/class/course-contain-cover")
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
        course.setModifier(userService.findByPhone(params.getParameter("modifier")).getId());
        String message = "";
        if(0 == files.size()) {
            message = courseService.editContainCover(course, null);
        } else {
            message = courseService.editContainCover(course, files.get(0));
        }
        if ("参数异常，修改失败".equals(message))
            return ResultUtil.buildFailResult(message);
        else
            return ResultUtil.buildSuccessResult(message,null);
    }

    @ApiOperation("删除课程")
    @ApiResponse(code = 200,message = "成功，data返回null" )
    @DeleteMapping("/api/class/courses")
    public Result deleteCourse(@RequestParam int cid) {
        log.info("---------------- 删除课程 ----------------------");
        String message = courseService.delete(cid);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("批量删除课程")
    @ApiResponse(code = 200,message = "成功，data返回null" )
    @DeleteMapping("/api/class/courses-batch")
    public Result batchDeleteCourse(@RequestBody List<Integer> courseIds) {
        log.info("---------------- 批量删除课程 ----------------------");
        String message = courseService.batchDelete(courseIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改课程除头像")
    @ApiResponse(code = 200,message = "成功，data返回null" )
    @PutMapping("/api/class/course")
    public Result eidtCourse(@RequestBody Course course) {
        log.info("---------------- 修改课程 ----------------------");
        String message = courseService.edit(course);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改课程封面")
    @ApiResponse(code = 200,message = "成功，data返回null" )
    @PutMapping("/api/class/course/cover")
    public Result updateCourseCover(HttpServletRequest request) throws Exception {
        log.info("---------------- 修改课程封面 ----------------------");
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        int cid = Integer.parseInt(params.getParameter("id"));
        String message = courseService.updateCover(files.get(0), cid);
        if (!"更新失败".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我创建的课程")
    @ApiResponse(code = 200,message = "成功，data返回Course类" )
    @GetMapping("/api/class/courses/getCreated")
    public Result getCurrentUserCreateCourse(@RequestParam String phone,
                                             @RequestParam int pageNum,
                                             @RequestParam int pageSize) {
        log.info("---------------- 获取我创建的课程 ----------------------");
        Page<Course> courses= courseService.findAllByCreatorId(userService.findByPhone(phone).getId(),pageNum,pageSize);
        Page<CourseDTO> courseDTO= PageUtil.pageChange(courses,CourseDTO.class);
        return ResultUtil.buildSuccessResult(courseDTO);
    }

    @ApiOperation("搜索课程")
    @ApiResponse(code = 200,message = "成功，data返回List<Course>类" )
    @GetMapping("/api/class/courses/search")
    public Result searchCourse(@RequestParam String keywords,
                               @RequestParam int pageNum,
                               @RequestParam int pageSize) {
        log.info("---------------- 搜索课程 ----------------------");
        Page<Course> courses= courseService.search(keywords,pageNum,pageSize);
        Page<CourseDTO> courseDTO= PageUtil.pageChange(courses,CourseDTO.class);
        return ResultUtil.buildSuccessResult(courseDTO);
    }

    @ApiOperation("获取所有课程")
    @ApiResponse(code = 200,message = "成功，data返回List<Course>类" )
    @GetMapping("/api/class/course/all")
    public Result getAllCourse(@RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("---------------- 获取所有课程 ----------------------");
        Page<Course> courses= courseService.findAll(pageNum,pageSize);
        Page<CourseDTO> courseDTO= PageUtil.pageChange(courses,CourseDTO.class);
        return ResultUtil.buildSuccessResult(courseDTO);
    }

    @ApiOperation("获取单一课程")
    @ApiResponse(code = 200,message = "成功，data返回course类" )
    @GetMapping("/api/class/stu/course/{cid}")
    public Result getById(@PathVariable("cid") int cid) {
        log.info("---------------- 获取单一课程 ----------------------");
        Course course= courseService.findById(cid);
        if (null != course){
            ModelMapper modelMapper = new ModelMapper();
            CourseDTO courseDTO=modelMapper.map(course,CourseDTO.class);
            return ResultUtil.buildSuccessResult(courseDTO);
        } else{
            return ResultUtil.buildFailResult("请输入正确课程Id");
        }
        }


    /** -------------------------- 课程学生表 -------------------------------------- **/


    @ApiOperation("获取课程学生")
    @ApiResponse(code = 200,message = "成功，data返回List<Map<String,String>>类" )
    @GetMapping("/api/class/stus/courses/stus")
    public Result getStudentsByCourseId(@RequestParam int cid) {
        log.info("---------------- 获取课程学生 ----------------------");
        List<Map<String,String>> courseStus = courseToStudentService.findAllStudentByCourseId(cid);
        return ResultUtil.buildSuccessResult(courseStus);
    }

    @ApiOperation("加入课程")
    @ApiResponse(code = 200,message = "成功，data返回null" )
    @PostMapping("/api/class/stus/courses")
    public Result joinCourse(@RequestParam int cid,@RequestParam String phone) {
        log.info("---------------- 加入课程 ----------------------");
        String message = courseToStudentService.joinCourse(userService.findByPhone(phone).getId(), cid);
        if ("加入成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我加入的课程")
    @ApiResponse(code = 200,message = "成功，data返回List<course>类" )
    @GetMapping("/api/class/stus/courses")
    public Result getCurrentUserJoinCourse(@RequestParam String phone) {
        log.info("---------------- 获取我加入的课程 ----------------------");
        List<Course> courses= courseToStudentService.findCourseByUserId(userService.findByPhone(phone).getId());
        return ResultUtil.buildSuccessResult(courses);
    }
}
