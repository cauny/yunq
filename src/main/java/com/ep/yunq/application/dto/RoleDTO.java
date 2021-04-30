package com.ep.yunq.application.dto;

import lombok.Data;

/**
 * @classname: RoleDTO
 * @Author: yan
 * @Date: 2021/4/24 12:02
 * 功能描述：
 **/
@Data
public class RoleDTO {
    private Integer id;
    private String name;
    private String nameZh;
    private Integer enabled;
}
