package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * @classname: PermissionResource
 * @Author: yan
 * @Date: 2021/3/28 13:12
 * 功能描述：
 **/
@Entity
@Table(name="permission")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class PermissionResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;    //
    private String code;    //角色代号
    private String uri;     //资源符号
    private String method;     //
    private Integer status;     //状态
    private String description;     //描述

}
