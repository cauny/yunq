package com.ep.yunq.application.dto;

import com.ep.yunq.domain.entity.AdminRole;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @classname: UserDTO
 * @Author: yan
 * @Date: 2021/4/30 12:03
 * 功能描述：
 **/
@Data
public class UserDTO {
    int id;
    private String username;    //用户名
    private String phone;   //手机号
    private String ino;
    private String school;
    private String major;
    private Integer enabled;       //是否使用
    private List<AdminRole> roles;
}
