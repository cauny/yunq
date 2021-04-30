package com.ep.yunq.controller;

import com.ep.yunq.application.dto.CourseAddDTO;
import com.ep.yunq.application.dto.CourseDTO;
import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.CourseService;
import com.ep.yunq.domain.service.CourseToStudentService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.ep.yunq.infrastructure.util.SmsUtil;
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
    @Autowired
    SmsUtil smsUtil;

    /**
     * -------------------------- 课程表 --------------------------------------
     **/

    @ApiOperation("添加课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "课程名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "string"),
            @ApiImplicitParam(name = "semester", value = "年级", required = true, dataType = "string"),
            @ApiImplicitParam(name = "school", value = "学校", required = true, dataType = "string"),
            @ApiImplicitParam(name = "college", value = "院系", required = true, dataType = "string"),
            @ApiImplicitParam(name = "major", value = "专业", required = true, dataType = "string"),
            @ApiImplicitParam(name = "teacher", value = "教师", required = true, dataType = "string"),
            @ApiImplicitParam(name = "learnRequire", value = "学习要求", required = false, dataType = "string"),
            @ApiImplicitParam(name = "teachProgress", value = "教学进度", required = false, dataType = "string"),
            @ApiImplicitParam(name = "examArrange", value = "考试安排", required = false, dataType = "string"),
            @ApiImplicitParam(name = "cover", value = "封面", required = true, dataType = "string"),
            @ApiImplicitParam(name = "creator", value = "创建者手机号", required = true, dataType = "string"),
    })
    @PostMapping("/api/classes/courses")
    public Result<CourseAddDTO> addCourse(HttpServletRequest request) {
        log.info("---------------- 添加课程 ----------------------");
        String message = "";
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
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
        course.setCreator(Integer.parseInt(params.getParameter("creator")));
        /*course.setSchoolId(Integer.parseInt(params.getParameter("schoolId")));
        course.setCollegeId(Integer.parseInt(params.getParameter("collegeId")));*/

        String code = courseService.createCourseCode();
        course.setCode(code);
        message = courseService.add(course, files.get(0));
        log.info(String.valueOf(files.get(0)));
        if ("参数异常，添加失败".equals(message)) {
            return ResultUtil.buildFailResult(message);
        } else {
            CourseAddDTO courseAddDTO = new CourseAddDTO();
            courseAddDTO.setCode(code);
            courseAddDTO.setImgUrl(message);
            return ResultUtil.buildSuccessResult(courseAddDTO);
        }

    }

    @ApiOperation("修改课程包含头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "name", value = "课程名字", required = true, dataType = "string"),
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "string"),
            @ApiImplicitParam(name = "semester", value = "年级", required = true, dataType = "string"),
            @ApiImplicitParam(name = "school", value = "学校", required = true, dataType = "string"),
            @ApiImplicitParam(name = "college", value = "院系", required = true, dataType = "string"),
            @ApiImplicitParam(name = "major", value = "专业", required = true, dataType = "string"),
            @ApiImplicitParam(name = "teacher", value = "教师", required = true, dataType = "string"),
            @ApiImplicitParam(name = "learnRequire", value = "学习要求", required = false, dataType = "string"),
            @ApiImplicitParam(name = "teachProgress", value = "教学进度", required = false, dataType = "string"),
            @ApiImplicitParam(name = "examArrange", value = "考试安排", required = false, dataType = "string"),
            @ApiImplicitParam(name = "cover", value = "封面", required = true, dataType = "string"),
            @ApiImplicitParam(name = "modifier", value = "修改者手机号", required = true, dataType = "string"),
    })
    @PutMapping("/api/classes/course-contain-covers")
    public Result<String> editCourseContainCover(HttpServletRequest request) {
        log.info("---------------- 修改课程包含封面 ----------------------");
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
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
        course.setModifier(Integer.parseInt(params.getParameter("modifier")));
        String message = "";
        if (0 == files.size()) {
            message = courseService.editContainCover(course, null);
        } else {
            message = courseService.editContainCover(course, files.get(0));
        }
        if ("参数异常，修改失败".equals(message))
            return ResultUtil.buildFailResult(message);
        else
            return ResultUtil.buildSuccessResult(message, null);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/api/classes/courses")
    public Result<String> deleteCourse(@RequestParam int cid) {
        log.info("---------------- 删除课程 ----------------------");
        String message = courseService.delete(cid);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message, null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("批量删除课程")
    @DeleteMapping("/api/classes/courses-batch")
    public Result<String> batchDeleteCourse(@RequestBody List<Integer> courseIds) {
        log.info("---------------- 批量删除课程 ----------------------");
        String message = courseService.batchDelete(courseIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message, null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改课程除头像")
    @PutMapping("/api/classes/courses")
    public Result<String> eidtCourse(@RequestBody Course course) {
        log.info("---------------- 修改课程 ----------------------");
        String message = courseService.edit(course);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message, null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改课程封面")
    @PutMapping("/api/classes/courses/cover")
    public Result<String> updateCourseCover(HttpServletRequest request) throws Exception {
        log.info("---------------- 修改课程封面 ----------------------");
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        int cid = Integer.parseInt(params.getParameter("id"));
        String message = courseService.updateCover(files.get(0), cid);
        if (!"更新失败".equals(message))
            return ResultUtil.buildSuccessResult(message, null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我创建的课程")
    @GetMapping("/api/classes/courses/uid/{userId}")
    public Result<Page<CourseDTO>> getCurrentUserCreateCourse(@PathVariable("userId") Integer userId,
                                                              @RequestParam int pageNum,
                                                              @RequestParam int pageSize) {
        log.info("---------------- 获取我创建的课程 ----------------------");
        Page<Course> courses = courseService.findAllByCreatorId(userId, pageNum, pageSize);
        Page<CourseDTO> courseDTO = PageUtil.pageChange(courses, CourseDTO.class);
        return ResultUtil.buildSuccessResult(courseDTO);
    }

    @ApiOperation("搜索课程")
    @GetMapping("/api/classes/courses/search")
    public Result<Page<CourseDTO>> searchCourse(@RequestParam String keywords,
                                                @RequestParam int pageNum,
                                                @RequestParam int pageSize) {
        log.info("---------------- 搜索课程 ----------------------");
        Page<Course> courses = courseService.search(keywords, pageNum, pageSize);
        Page<CourseDTO> courseDTO = PageUtil.pageChange(courses, CourseDTO.class);
        return ResultUtil.buildSuccessResult(courseDTO);
    }

    @ApiOperation("获取所有课程")
    @GetMapping("/api/classes/courses")
    public Result<Page<CourseDTO>> getAllCourse(@RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("---------------- 获取所有课程 ----------------------");
        Page<Course> courses = courseService.findAll(pageNum, pageSize);
        Page<CourseDTO> courseDTO = PageUtil.pageChange(courses, CourseDTO.class);
        return ResultUtil.buildSuccessResult(courseDTO);
    }

    @ApiOperation("获取单一课程")
    @GetMapping("/api/classes/courses/cid/{cid}")
    public Result<CourseDTO> getById(@PathVariable("cid") int cid) {
        log.info("---------------- 获取单一课程 ----------------------");
        Course course = courseService.findById(cid);
        if (null != course) {
            ModelMapper modelMapper = new ModelMapper();
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
            return ResultUtil.buildSuccessResult(courseDTO);
        } else {
            return ResultUtil.buildFailResult("请输入正确课程Id");
        }
    }


    /**
     * -------------------------- 课程学生表 --------------------------------------
     **/


    @ApiOperation("获取课程学生")
    @GetMapping("/api/classes/students")
    public Result<List<Map<String, String>>> getStudentsByCourseId(@RequestParam int cid) {
        log.info("---------------- 获取课程学生 ----------------------");
        List<Map<String, String>> courseStus = courseToStudentService.findAllStudentByCourseId(cid);
        return ResultUtil.buildSuccessResult(courseStus);
    }

    @ApiOperation("加入课程")
    @PostMapping("/api/classes/students/courses")
    public Result<String> joinCourse(@RequestParam int cid, @RequestParam Integer userId) {
        log.info("---------------- 加入课程 ----------------------");
        String message = courseToStudentService.joinCourse(userId, cid);
        if ("加入成功".equals(message))
            return ResultUtil.buildSuccessResult(message, null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我加入的课程")
    @GetMapping("/api/classes/students/courses")
    public Result<List<Course>> getCurrentUserJoinCourse(@RequestParam Integer userId) {
        log.info("---------------- 获取我加入的课程 ----------------------");
        List<Course> courses = courseToStudentService.findCourseByUserId(userId);
        return ResultUtil.buildSuccessResult(courses);
    }
}
