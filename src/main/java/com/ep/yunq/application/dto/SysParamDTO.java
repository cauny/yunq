package com.ep.yunq.application.dto;

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
    private String signinExperience;
    private String signinRange;
    private String classTime;
    private String level1;
    private String level2;
    private String level3;
    private Date updateTime;

    public SysParamDTO() {
    }
}
