package com.ep.yunq.pojo;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

/**
 * @classname: UserBasicInfo
 * @Author: yan
 * @Date: 2021/4/12 21:58
 * 功能描述：
 **/
@Data
public class UserBasicInfo {
    private String username;
    private String phone;
    private String avatar;
    private List<String> roles;
    private String defaultRole;

}
