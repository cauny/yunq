package com.ep.yunq.application.dto;

import lombok.Data;

import java.util.List;

/**
 * @classname: UserDTO
 * @Author: yan
 * @Date: 2021/4/20 10:56
 * 功能描述：
 **/
@Data
public class UserDTO {
    int id;
    private String username;
    private String phone;
    private String avatar;
    private List<String> roles;
    private String defaultRole;

    public UserDTO() {
    }

    public UserDTO(String username, String phone, String avatar, List<String> roles, String defaultRole) {
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
        this.roles = roles;
        this.defaultRole = defaultRole;
    }
}
