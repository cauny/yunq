package com.ep.yunq.util;

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
    INTERNAL_SERVER_ERROR(500);



    public int code;
    ConstantUtil(int code) {    this.code=code;}
}
