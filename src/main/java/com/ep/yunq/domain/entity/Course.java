package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @classname: course
 * @Author: yan
 * @Date: 2021/4/4 17:24
 * 功能描述： 课程表
 **/
@Entity
@Table(name = "course")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    private String name;    //
    private String grade;    //
    private String semester;    //
    private String school;    //
    private String college;    //
    private String major;    //
    private String teacher;    //
    private String learnRequire;    //
    private String teachProgress;    //
    private String examArrange;    //
    private String cover;    //
    private String qrcode;    //
    private int creator;
    private int modifier;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "creation_date")
    private Date creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modifition_date")
    private Date modifitionDate;
}
