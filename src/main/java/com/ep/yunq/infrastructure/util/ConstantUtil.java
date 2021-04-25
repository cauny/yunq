package com.ep.yunq.infrastructure.util;

import org.springframework.stereotype.Component;

/**
 * @classname: ConstantUtil
 * @Author: yan
 * @Date: 2021/3/27 10:01
 * 功能描述：存放常量的枚举类
 **/
public enum ConstantUtil {
    //请求返回
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    USER_INFO_UNCOMPLETE(406),
    PHONE_IS_USED(409),
    VERIFIED_CODE_ERROR(407),
    INTERNAL_SERVER_ERROR(500),


    //签到
    SIGNUP_REPEAT(701),
    SIGNUP_OUT_RANGE(702),
    SIGNUP_VALUE_ERROR(703),
    //短信验证码
    SMS_Verification_Code(801),
    //系统参数
    Sys_Param_Experience(2),
    Sys_Param_distance(100),

    //文件
    FILE_Url_Course("http://59.77.134.88:8080/api/file/Course/"),
    FILE_Url_User("http://59.77.134.88:8080/api/file/User/"),
    FILE_Url_QrCode("http://59.77.134.88:8080/api/file/QrCode/"),
    FILE_Url_Base("http://59.77.134.88:8080/api/file/"),
    FILE_Photo_User("C:/img/User/"),
    FILE_Photo_Course("C:/img/Course/"),
    FILE_QrCode("C:/img/QrCode/");


    public int code;
    public String string;
    ConstantUtil(int code) {    this.code=code;}
    ConstantUtil(String string) {
        this.string = string;
    }
}
