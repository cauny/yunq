package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.AllStudentSignInCourseDTO;
import com.ep.yunq.application.dto.CourseStudentSignInDTO;
import com.ep.yunq.domain.dao.StudentSignInDAO;
import com.ep.yunq.domain.entity.CourseSignIn;
import com.ep.yunq.domain.entity.StudentSignIn;
import com.ep.yunq.infrastructure.util.CommonUtil;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @classname: StudentSignInService
 * @Author: yan
 * @Date: 2021/4/13 10:17
 * 功能描述：
 **/
@Slf4j
@Service
public class StudentSignInService {

    @Autowired
    StudentSignInDAO studentSignInDAO;
    @Autowired
    UserService userService;
    @Autowired
    CourseToStudentService courseStudentService;
    @Autowired
    CourseSignInService courseSignInService;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void addOrUpdate(StudentSignIn studentSignIn){
        /*studentSignIn.setStudentId(userService.getCurrentUserId());*/
        studentSignIn.setTime(new Date());
        studentSignInDAO.save(studentSignIn);
    }

    public boolean isSignIn(int cid, int uid){
        StudentSignIn studentSignIn = studentSignInDAO.findByCourseSignInAndStudentId(cid, uid);
        if (null == studentSignIn)
            return false;
        else
            return true;
    }

    public String add(StudentSignIn studentSignIn) {
        String message = "";
        int uid = studentSignIn.getStudentId();
        try {
            if (isSignIn(studentSignIn.getCourseSignIn().getId(), uid)){
                return "请勿重复签到！";
            }
            if (ConstantUtil.SIGNUP_Mode_Time.string.equals(studentSignIn.getMode())) {
                GlobalCoordinates source = new GlobalCoordinates(studentSignIn.getLatitude().doubleValue(), studentSignIn.getLongitude().doubleValue());
                GlobalCoordinates target = new GlobalCoordinates(studentSignIn.getCourseSignIn().getLatitude().doubleValue(), studentSignIn.getCourseSignIn().getLongitude().doubleValue());
                double dist = CommonUtil.getDistanceMeter(source, target, Ellipsoid.Sphere);
                if (dist >= ConstantUtil.Sys_Param_distance.code){
                    message = "超出签到范围";
                } else if (studentSignIn.getTime().after(studentSignIn.getCourseSignIn().getEndTime())) {
                    message = "超过时间";
                } else {
                    addOrUpdate(studentSignIn);
                    courseStudentService.addExperience(studentSignIn.getCourseSignIn().getCourse().getId(), uid);
                    message = "签到成功";
                }
            } else if (ConstantUtil.SIGNUP_Mode_Gesture.string.equals(studentSignIn.getMode())) {
                GlobalCoordinates source = new GlobalCoordinates(studentSignIn.getLatitude().doubleValue(), studentSignIn.getLongitude().doubleValue());
                GlobalCoordinates target = new GlobalCoordinates(studentSignIn.getCourseSignIn().getLatitude().doubleValue(), studentSignIn.getCourseSignIn().getLongitude().doubleValue());
                double dist = CommonUtil.getDistanceMeter(source, target, Ellipsoid.Sphere);
                if (dist >= ConstantUtil.Sys_Param_distance.code){
                    message = "超出签到范围";
                } else if (!studentSignIn.getValue().equals(studentSignIn.getCourseSignIn().getValue())) {
                    message = "手势错误";
                } else {
                    addOrUpdate(studentSignIn);
                    courseStudentService.addExperience(studentSignIn.getCourseSignIn().getCourse().getId(), uid);
                    message = "签到成功";
                }
            } else {
                message = "参数异常，签到失败";
            }
        } catch (Exception e) {
            message = "参数异常，签到失败";
            e.printStackTrace();
        }
        return message;
    }

    /*public List<Map<String,Object>> getAllSignInByUserId(int uid, int cid){
        List<StudentSignIn> studentSignIns = studentSignInDAO.findAllByStudentIdAndCourseSignIn(uid, cid);
        List<CourseSignIn> courseSignIns = courseSignInService.listAllByCourse(cid);
        List<Map<String,Object>> maps = new ArrayList<>();
        for (CourseSignIn courseSignIn: courseSignIns){
            Map<String,Object> map = new HashMap<>();
            map.put("csiid",courseSignIn.getId());
            map.put("time",sdf.format(courseSignIn.getStartTime()));
            map.put("mode",courseSignIn.getMode());
            map.put("isSignIn",false);
            for (StudentSignIn studentSignIn: studentSignIns){
                if (courseSignIn.getId() == studentSignIn.getCourseSignIn().getId()){
                    map.put("isSignIn",true);
                    break;
                }
            }
            maps.add(map);

        }
        return maps;
    }*/
    public List<CourseStudentSignInDTO> getAllSignInByUserId(int uid, int cid){
        List<StudentSignIn> studentSignIns = studentSignInDAO.findAllByStudentIdAndCourseSignIn(uid, cid);
        List<CourseSignIn> courseSignIns = courseSignInService.listAllByCourse(cid);
        List<CourseStudentSignInDTO> maps = new ArrayList<>();
        for (CourseSignIn courseSignIn: courseSignIns){
            CourseStudentSignInDTO courseStudentSignInDTO=new CourseStudentSignInDTO();
            courseStudentSignInDTO.setCourseId(courseSignIn.getId());
            courseStudentSignInDTO.setMode(courseSignIn.getMode());
            courseStudentSignInDTO.setTime(sdf.format(courseSignIn.getStartTime()));
            courseStudentSignInDTO.setIsSignIn(false);
            for (StudentSignIn studentSignIn: studentSignIns){
                if (courseSignIn.getId() == studentSignIn.getCourseSignIn().getId()){
                    courseStudentSignInDTO.setIsSignIn(true);
                    break;
                }
            }
            maps.add(courseStudentSignInDTO);
        }
        return maps;
    }

    public List<AllStudentSignInCourseDTO> getAllSignInByCourseSignIn(int csiid){
        List<Map<String,Object>> maps = studentSignInDAO.findAllByCourseSignIn(csiid);
        List<AllStudentSignInCourseDTO> result = new ArrayList<>();
        for (Map<String,Object> map: maps){
            AllStudentSignInCourseDTO tmp=new AllStudentSignInCourseDTO();
            tmp.setIno((String) map.get("ino"));
            tmp.setName((String) map.get("name"));
            tmp.setTime(sdf.format(map.get("time")));
            tmp.setMode((String) map.get("mode"));
            result.add(tmp);
        }
        return result;
    }

}
