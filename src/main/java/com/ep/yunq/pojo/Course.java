package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @classname: course
 * @Author: yan
 * @Date: 2021/4/4 17:24
 * 功能描述： 课程表
 **/
@Entity
@Table(name="course")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;
    private String name;    //
    private String grade;    //
    private String semester;    //
    private String school;    //
    private String college;    //
    private String major;    //
    private String teacher;    //
    private String learn_require;    //
    private String teach_progress;    //
    private String exam_arrange;    //
    private String cover;    //
    private String qrcode;    //
    private Timestamp update_time;    //
}
