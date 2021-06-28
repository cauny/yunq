package com.ep.yunq.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname: CourseSignInDTO
 * @Author: yan
 * @Date: 2021/4/28 20:53
 * 功能描述：
 **/
@Data
public class CourseSignInDTO {
    private int id;
    private String mode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    /*private Integer status;*/
    private Integer isFinished;
    /*private Integer countDown;*/
}
