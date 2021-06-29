package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.StudentDTO;
import com.ep.yunq.domain.dao.CourseToStudentDAO;
import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.CourseToStudent;
import com.ep.yunq.domain.entity.SysParam;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @classname: CourseToStudentService
 * @Author: yan
 * @Date: 2021/4/13 10:06
 * 功能描述：
 **/
@Slf4j
@Service
public class CourseToStudentService {
    @Autowired
    CourseToStudentDAO courseToStudentDAO;
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    SysParamService sysParamService;

    public CourseToStudent findByCourseIdAndUserId(int cid,int uid){
        return courseToStudentDAO.findByCourseAndUser(cid,uid);
    }

    public void addOrUpdate(CourseToStudent courseToStudent) {
        courseToStudent.setUpdateTime(new Date());
        courseToStudentDAO.save(courseToStudent);
    }

    public boolean isJoin(int uid, String code){
        CourseToStudent courseToStudent = courseToStudentDAO.findByCourseCodeAndUser(code, uid);
        if (null == courseToStudent)
            return false;
        else
            return true;
    }

    public String joinCourse(int uid,String code) {
        String message = "";
        try {
            if (isJoin(uid, code)){
                return "请勿重复加入课程";
            }
            User user = userService.findById(uid);
            Course course = courseService.findByCode(code);
            if(course==null){
                return "该课程不存在";
            }
            CourseToStudent courseToStudent = new CourseToStudent();
            courseToStudent.setExperience(0);
            courseToStudent.setStu(user);
            courseToStudent.setCourse(course);

            addOrUpdate(courseToStudent);
            message = "加入成功";

        } catch (Exception e) {
            message = "参数异常，加入失败";
            e.printStackTrace();
        }
        return message;
    }

    public Integer findCourseIdByCode(String code){
        Course course=courseService.findByCode(code);
        if(course==null){
            return null;
        }
        return course.getId();
    }

    public List<StudentDTO> findAllStudentByCourseId(int cid) {
        List<Map<String,String>> temps = courseToStudentDAO.findAllUserByCourseId(cid);
        List<StudentDTO> studentDTOS=new ArrayList<>();
        for (Map temp:temps) {
            StudentDTO studentDTO=new StudentDTO();
            studentDTO.setIno(String.valueOf(temp.get("ino")));
            studentDTO.setUserId(Integer.valueOf(String.valueOf(temp.get("user_id"))));
            studentDTO.setExperience(Integer.valueOf(String.valueOf(temp.get("experience"))));
            studentDTO.setUsername(String.valueOf(temp.get("username")));
            studentDTO.setCover((String) temp.get("avatar"));
            studentDTO.setLevel((String) temp.get("level"));
            studentDTOS.add(studentDTO);
        }
        return studentDTOS;
    }

    public List<Course> findCourseByUserId(int uid){
        List<Course> courses = courseToStudentDAO.findCourseByUserId(uid);
        for (Course course:courses) {
            course.setCover(ConstantUtil.FILE_Url_Course.string+course.getCover());
            course.setQrcode(ConstantUtil.FILE_Url_QrCode.string+course.getQrcode());
        }
        return courses;
    }

    public void addExperience(int cid,int uid){
        CourseToStudent courseToStudentInDB = courseToStudentDAO.findByCourseAndUser(cid, uid);
        SysParam sysParam=sysParamService.findByUserIdAndName(uid,"signInExperience");
        log.info("c:"+String.valueOf(courseToStudentInDB==null));
        log.info("s:"+String.valueOf(sysParam==null));
        int experience = courseToStudentInDB.getExperience() +Integer.parseInt(sysParam.getValue());
        courseToStudentInDB.setExperience(experience);
        if(experience>Integer.parseInt(sysParamService.findByUserIdAndName(uid,"level_3").getValue())){
            courseToStudentInDB.setLevel("优秀");
        }else if(experience>Integer.parseInt(sysParamService.findByUserIdAndName(uid,"level_2").getValue())){
            courseToStudentInDB.setLevel("良好");
        }else if(experience>Integer.parseInt(sysParamService.findByUserIdAndName(uid,"level_1").getValue())){
            courseToStudentInDB.setLevel("中等");
        }else{
            courseToStudentInDB.setLevel("不合格");
        }

        addOrUpdate(courseToStudentInDB);
    }

}
