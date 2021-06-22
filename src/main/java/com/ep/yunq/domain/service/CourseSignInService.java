package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.CourseSignInDAO;
import com.ep.yunq.domain.entity.CourseSignIn;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;

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
    @Autowired
    CourseService courseService;

    public CourseSignIn addOrUpdate(CourseSignIn courseSignIn) {
        return courseSignInDAO.save(courseSignIn);
    }

    public CourseSignIn findById(int id){ return courseSignInDAO.findById(id);}

    public String add(CourseSignIn courseSignIn,String code) {
        String message = "";
        try {
            if(ConstantUtil.SIGNUP_Mode_Time.string.equals(courseSignIn.getMode())) {
                courseSignIn.setStartTime(new Date());
                Calendar endTime = Calendar.getInstance();
                endTime.add(Calendar.MINUTE, courseSignIn.getValue());
                Date endDate = (Date) endTime.getTime();
                courseSignIn.setEndTime(endDate);

                Instant instant = endDate.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

                courseSignIn.setCourse(courseService.findByCode(code));
                courseSignIn.setIsFinished(0);
                CourseSignIn courseSignIn1=addOrUpdate(courseSignIn);

                timeTrigger(localDateTime,courseSignIn1.getId());

            } else if (ConstantUtil.SIGNUP_Mode_OneStep.string.equals(courseSignIn.getMode())) {
                courseSignIn.setStartTime(new Date());
                courseSignIn.setCourse(courseService.findByCode(code));
                courseSignIn.setIsFinished(0);
                addOrUpdate(courseSignIn);
            } else {
                return "请选择正确签到模式!";
            }

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
            courseSignInInDB.setIsFinished(1);
            addOrUpdate(courseSignInInDB);
            message = "结束签到";
        } catch (Exception e) {
            message = "参数异常，结束失败";
            e.printStackTrace();
        }
        return message;
    }

    public void timeTrigger(LocalDateTime localDateTime, Integer csid){
        Calendar calendar = Calendar.getInstance();

        /**
         * 指定触发的时间
         * */
        calendar.set(Calendar.YEAR, localDateTime.get(ChronoField.YEAR)); // 设置年份
        calendar.set(Calendar.DAY_OF_MONTH, localDateTime.get(ChronoField.DAY_OF_MONTH));//设置日期
        calendar.set(Calendar.MONTH, localDateTime.get(ChronoField.MONTH_OF_YEAR)-1);//设置日期为月份   这里3表示4月份    4就表示5月份
        calendar.set(Calendar.HOUR_OF_DAY, localDateTime.get(ChronoField.HOUR_OF_DAY)); //设置触发时
        calendar.set(Calendar.MINUTE, localDateTime.get(ChronoField.MINUTE_OF_HOUR)); //设置触发分
        calendar.set(Calendar.SECOND, localDateTime.get(ChronoField.SECOND_OF_MINUTE)); //设置触发秒

        Date time = calendar.getTime();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CourseSignIn courseSignIn=findById(csid);
                courseSignIn.setIsFinished(1);
                addOrUpdate(courseSignIn);
            }
        }, time);

    }
}
