package com.ep.yunq.dto;

import lombok.Data;

import java.util.Date;

/**
 * @classname: SysParamDTO
 * @Author: yan
 * @Date: 2021/4/20 9:46
 * 功能描述：
 **/
@Data
public class SysParamDTO {
    private int id;
    private String key1;    //signin_experience
    private String value1;  //系统参数值
    private String key2;  //signin_range
    private String value2;  //系统参数值
    private String key3;  //class_time
    private String value3;  //系统参数值
    private Date updateTime;

    public SysParamDTO() {
    }
}
