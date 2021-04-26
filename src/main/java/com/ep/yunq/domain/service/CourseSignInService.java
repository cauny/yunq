package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.CourseSignInDAO;
import com.ep.yunq.domain.entity.CourseSignIn;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @classname: CourseSignInService
 * @Author: yan
 * @Date: 2021/4/13 10:16
 * 功能描述：
 **/
@Slf4j
@Service
public class CourseSignInService {

    @Autowired
    CourseSignInDAO courseSignInDAO;

    public void addOrUpdate(CourseSignIn courseSignIn) {
        courseSignInDAO.save(courseSignIn);
    }

    public String add(CourseSignIn courseSignIn) {
        String message = "";
        try {
            if(ConstantUtil.SIGNUP_Mode_Time.string.equals(courseSignIn.getMode())) {
                courseSignIn.setStartTime(new Date());
                Calendar endTime = Calendar.getInstance();
                endTime.add(Calendar.MINUTE, courseSignIn.getValue());
                Date endDate = (Date) endTime.getTime();
                courseSignIn.setEndTime(endDate);
            } else if (ConstantUtil.SIGNUP_Mode_Gesture.string.equals(courseSignIn.getMode())) {
                courseSignIn.setStartTime(new Date());
            } else {
                return "请选择正确签到模式!";
            }
            addOrUpdate(courseSignIn);
            message = "创建成功";
        } catch (Exception e) {
            message = "参数异常，创建失败";
            e.printStackTrace();
        }
        return message;
    }

    public List<CourseSignIn> listAllByCourse(int cid) {
        return courseSignInDAO.findAllByCourse(cid);
    }

    public CourseSignIn getCurrentSignInByCourseId(int cid){
        CourseSignIn courseSignIn = courseSignInDAO.findByCourseIdAndDate(cid, new Date());

        return courseSignIn;
    }

    public String endSignIn(int cspid){
        String message = "";
        try {
            CourseSignIn courseSignInInDB = courseSignInDAO.findById(cspid);
            courseSignInInDB.setEndTime(new Date());
            addOrUpdate(courseSignInInDB);
            message = "结束签到";
        } catch (Exception e) {
            message = "参数异常，结束失败";
            e.printStackTrace();
        }
        return message;
    }
}
