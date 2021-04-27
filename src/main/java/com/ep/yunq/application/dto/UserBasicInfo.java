package com.ep.yunq.application.dto;

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
    private String username;
    private String phone;
    private String avatar;
    private List<String> roles;
    private String defaultRole;

    public UserBasicInfo() {
    }

    public UserBasicInfo(String username, String phone, String avatar, List<String> roles, String defaultRole) {
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
        this.roles = roles;
        this.defaultRole = defaultRole;
    }
}
