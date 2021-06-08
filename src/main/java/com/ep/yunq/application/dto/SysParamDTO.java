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
    private String name;
    private String nameZh;
    private String value;

    public SysParamDTO() {
    }
}
