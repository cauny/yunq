package com.ep.yunq.application.dto;

import lombok.Data;

import java.util.Date;

/**
 * @classname: StudentSignInDTO
 * @Author: yan
 * @Date: 2021/5/3 10:56
 * 功能描述：
 **/
@Data
public class CourseStudentSignInDTO {
    private Integer courseId;
    private String time;
    private String mode;
    private Boolean isSignIn;
}
