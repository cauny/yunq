package com.ep.yunq.domain.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @classname: UserBasicInfo
 * @Author: yan
 * @Date: 2021/4/12 21:58
 * 功能描述：
 **/
@ApiModel(value = "返回用户信息")
@Data
public class UserBasicInfo {
    private Integer id;
    private String username;
    private String phone;
    private String avatar;
    private String ino;
    private List<String> roles;
    private String defaultRole;

    public UserBasicInfo() {
    }

    public UserBasicInfo(Integer id, String username, String phone, String avatar, String ino, List<String> roles, String defaultRole) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
        this.ino = ino;
        this.roles = roles;
        this.defaultRole = defaultRole;
    }
}
