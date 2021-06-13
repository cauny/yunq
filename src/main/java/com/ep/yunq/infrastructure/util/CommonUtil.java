package com.ep.yunq.infrastructure.util;

import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.service.CourseService;
import com.usthe.sureness.subject.SubjectSum;
import com.usthe.sureness.util.SurenessContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @classname: CommonUtil
 * @Author: yan
 * @Date: 2021/4/12 11:39
 * 功能描述：
 **/
@Slf4j
public class CommonUtil {

    @Autowired
    CourseService courseService;

    public static String creatUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid){

        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }

    public static Course multipartRequestChangeToCourse(MultipartHttpServletRequest params){
        return new Course(params.getParameter("name"), params.getParameter("grade"), params.getParameter("semester"),
                params.getParameter("school"), params.getParameter("college"), params.getParameter("major"), params.getParameter("teacher"),
                params.getParameter("learnRequire"), params.getParameter("teachProgress"), params.getParameter("examArrange"));
    }
    public static Course requestChangeToCourse(HttpServletRequest params){
        return new Course(params.getParameter("name"), params.getParameter("grade"), params.getParameter("semester"),
                params.getParameter("school"), params.getParameter("college"), params.getParameter("major"), params.getParameter("teacher"),
                params.getParameter("learnRequire"), params.getParameter("teachProgress"), params.getParameter("examArrange"));
    }

    public static Integer getTokenId(){
        SubjectSum subject = SurenessContextHolder.getBindSubject();
        log.info(String.valueOf(subject==null));
        if (subject == null || subject.getPrincipal() == null) {
            return null;
        }
        String appId = (String) subject.getPrincipal();
        Integer id=Integer.parseInt(appId);
        return id;
    }


}
