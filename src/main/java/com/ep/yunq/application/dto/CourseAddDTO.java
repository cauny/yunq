package com.ep.yunq.application.dto;

import lombok.Data;

/**
 * @classname: CourseAddDTO
 * @Author: yan
 * @Date: 2021/4/28 19:17
 * 功能描述：添加Course后返回对象
 **/
@Data
public class CourseAddDTO {
    private String code;
    private String imgUrl;
}
