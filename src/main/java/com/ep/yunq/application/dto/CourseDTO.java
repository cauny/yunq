package com.ep.yunq.application.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @classname: CourseDTO
 * @Author: yan
 * @Date: 2021/4/23 11:01
 * 功能描述：
 **/
@Data
public class CourseDTO {
    private int id;
    private String code;
    private String name;    //
    private String className;    //
    private String grade;    //
    private String semester;    //
    private String school;    //
    private String college;    //
    private String major;    //
    private String teacher;    //
    private String learnRequire;    //
    private String teachProgress;    //
    private String examArrange;    //
    private String cover;    //
    private String qrcode;    //
}
