package com.ep.yunq.application.dto;

import com.ep.yunq.domain.entity.UserBasicInfo;
import lombok.Data;

/**
 * @classname: UserDTO
 * @Author: yan
 * @Date: 2021/4/20 10:56
 * 功能描述：
 **/
@Data
public class UserLoginDTO {
    private UserBasicInfo userInfo;
    private String token;

    public UserLoginDTO() {
    }

}
