package com.ep.yunq.application.dto;

import lombok.Data;

/**
 * @classname: StudentDTO
 * @Author: yan
 * @Date: 2021/6/26 10:43
 * 功能描述：
 **/
@Data
public class StudentDTO {

    private Integer userId;
    private String cover;
    private String ino;
    private Integer experience;
    private String username;
    private String level;

}
