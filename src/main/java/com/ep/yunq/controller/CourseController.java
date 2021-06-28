package com.ep.yunq.controller;

import com.ep.yunq.application.dto.CourseAddDTO;
import com.ep.yunq.application.dto.CourseDTO;
import com.ep.yunq.application.dto.StudentDTO;
import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.CourseService;
import com.ep.yunq.domain.service.CourseToStudentService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.CommonUtil;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.ep.yunq.infrastructure.util.SmsUtil;
import com.usthe.sureness.mgt.SurenessSecurityManager;
import com.usthe.sureness.subject.SubjectSum;
import com.usthe.sureness.util.SurenessContextHolder;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @classname: CourseController
 * @Author: yan
 * @Date: 2021/4/13 10:05
 * 功能描述：
 **/

@Api(tags = "班课管理")
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
            @ApiImplicitParam(name = "name", value = "课程名字", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "className", value = "班级名字", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "semester", value = "年级", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "school", value = "学校", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "college", value = "院系", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "major", value = "专业", dataTypeClass = String.class),
            @ApiImplicitParam(name = "teacher", value = "教师", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "learnRequire", value = "学习要求", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "teachProgress", value = "教学进度", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "examArrange", value = "考试安排", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "cover", value = "封面", required = true, dataTypeClass = String.class),
    })
    @PostMapping("/api/classes/courses")
    public Result<CourseAddDTO> addCourse(HttpServletRequest request) {
        log.info("---------------- 添加课程 ----------------------");
        String message = "";

        //获取token中的用户id
        Integer uid=CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }

        MultipartHttpServletRequest params = null;
        List<MultipartFile> files=new ArrayList<>();
        Course course=new Course();
        String code = courseService.createCourseCode();
        if (request instanceof MultipartHttpServletRequest) {

            params = (MultipartHttpServletRequest) (request);
            files = ((MultipartHttpServletRequest) request)
                    .getFiles("cover");
            course= CommonUtil.multipartRequestChangeToCourse(params);
        } else {
            files.add(0,null);
            log.info(String.valueOf(files.get(0)));
            log.info("String.valueOf(files.get(0))");
            course=CommonUtil.requestChangeToCourse(request);

        }
        course.setCreator(uid);
        course.setCode(code);

        /*course.setSchoolId(Integer.parseInt(params.getParameter("schoolId")));
        course.setCollegeId(Integer.parseInt(params.getParameter("collegeId")));*/

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
            @ApiImplicitParam(name = "id", value = "课程id", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "课程名字", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "className", value = "班级名字", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "semester", value = "年级", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "school", value = "学校", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "college", value = "院系", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "major", value = "专业", dataTypeClass = String.class),
            @ApiImplicitParam(name = "teacher", value = "教师", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "learnRequire", value = "学习要求", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "teachProgress", value = "教学进度", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "examArrange", value = "考试安排", required = false, dataTypeClass = String.class),
            @ApiImplicitParam(name = "cover", value = "封面", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "modifier", value = "修改者id", required = true, dataTypeClass = String.class),
    })
    @PutMapping("/api/classes/course-contain-covers")
    public Result<String> editCourseContainCover(HttpServletRequest request) {
        log.info("---------------- 修改课程包含封面 ----------------------");
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("cover");
        Integer uid=CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        Course course = new Course();
        course.setId(Integer.parseInt(params.getParameter("id")));
        course.setName(params.getParameter("name"));
        course.setName(params.getParameter("className"));
        course.setGrade(params.getParameter("grade"));
        course.setSemester(params.getParameter("semester"));
        course.setSchool(params.getParameter("school"));
        course.setCollege(params.getParameter("college"));
        course.setMajor(params.getParameter("major"));
        course.setTeacher(params.getParameter("teacher"));
        course.setLearnRequire(params.getParameter("learnRequire"));
        course.setTeachProgress(params.getParameter("teachProgress"));
        course.setExamArrange(params.getParameter("examArrange"));
        course.setModifier(uid);
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
    @GetMapping("/api/classes/courses/uid")
    public Result<Page<CourseDTO>> getCurrentUserCreateCourse(@RequestParam int pageNum,
                                                              @RequestParam int pageSize) {
        log.info("---------------- 获取我创建的课程 ----------------------");
        Integer uid=CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        Page<Course> courses = courseService.findAllByCreatorId(uid, pageNum, pageSize);
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

    @ApiOperation("根据code获取课程")
    @GetMapping("/api/classes/courses/codes")
    public Result<CourseDTO> getByCode(String code) {
        log.info("---------------- 根据code获取课程 ----------------------");
        Course course=courseService.findByCode(code);
        if (null != course) {
            ModelMapper modelMapper = new ModelMapper();
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
            return ResultUtil.buildSuccessResult(courseDTO);
        } else {
            return ResultUtil.buildFailResult("请输入正确课程编码");
        }
    }

    /**
     * -------------------------- 课程学生表 --------------------------------------
     **/


    @ApiOperation("获取课程学生")
    @GetMapping("/api/classes/students")
    public Result<List<StudentDTO>> getStudentsByCourseId(@RequestParam String code) {
        log.info("---------------- 获取课程学生 ----------------------");
        Integer cid= courseToStudentService.findCourseIdByCode(code);
        if(cid==null){
            return ResultUtil.buildFailResult("该课程不存在");
        }
        List<StudentDTO> courseStus = courseToStudentService.findAllStudentByCourseId(cid);
        return ResultUtil.buildSuccessResult(courseStus);
    }

    @ApiOperation("加入课程")
    @PostMapping("/api/classes/students/courses")
    public Result<String> joinCourse(@RequestParam String code) {
        log.info("---------------- 加入课程 ----------------------");
        Integer uid=CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        String message = courseToStudentService.joinCourse(uid, code);
        if ("加入成功".equals(message))
            return ResultUtil.buildSuccessResult(message, null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取我加入的课程")
    @GetMapping("/api/classes/students/courses")
    public Result<List<Course>> getCurrentUserJoinCourse() {
        log.info("---------------- 获取我加入的课程 ----------------------");
        Integer uid=CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        List<Course> courses = courseToStudentService.findCourseByUserId(uid);
        return ResultUtil.buildSuccessResult(courses);
    }
}
