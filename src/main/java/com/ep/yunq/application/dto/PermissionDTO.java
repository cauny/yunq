package com.ep.yunq.application.dto;

import lombok.Data;

/**
 * @classname: PermissionDTO
 * @Author: yan
 * @Date: 2021/4/28 20:43
 * 功能描述：
 **/
@Data
public class PermissionDTO {
    private Integer id;
    private String name;    //
    private String code;    //角色代号
    private String uri;     //资源符号
    private String method;     //
    private Integer status;     //状态
    private String description;     //描述
}
