package com.ep.yunq.application.dto;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @classname: AllStudentSignInCourseDTO
 * @Author: yan
 * @Date: 2021/5/3 11:05
 * 功能描述：
 **/
@Data
public class AllStudentSignInCourseDTO {
    private String ino;
    private String name;
    private String time;
    private String mode;
}
