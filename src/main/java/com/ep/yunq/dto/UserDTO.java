package com.ep.yunq.dto;

import lombok.Data;

/**
 * @classname: UserDTO
 * @Author: yan
 * @Date: 2021/4/20 10:56
 * 功能描述：
 **/
@Data
public class UserDTO {
    int id;
    private String username;    //用户名
    private String phone;   //手机号
}
