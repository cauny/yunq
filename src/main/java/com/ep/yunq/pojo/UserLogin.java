package com.ep.yunq.pojo;

import lombok.Data;

/**
 * @classname: UserLogin
 * @Author: yan
 * @Date: 2021/3/29 22:10
 * 功能描述：这个类只用于登录获取json
 **/
@Data
public class UserLogin {
    int id;
    private String username;    //用户名
    private String phone;   //手机号
    private String email;   //邮箱号
    private String password;    //密码
    private Boolean rememberMe;
}
