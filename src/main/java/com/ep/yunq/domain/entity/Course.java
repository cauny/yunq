package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.auth.In;
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
    private String className;
    private String code;
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
    private Integer creator;
    private Integer modifier;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "creation_date")
    private Date creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modification_date")
    private Date modificationDate;


    public Course() {
    }

    public Course(String name,String className,String grade, String semester, String school, String college,
                  String major, String teacher, String learnRequire, String teachProgress,
                  String examArrange) {
        this.name = name;
        this.className=className;
        this.grade = grade;
        this.semester = semester;
        this.school = school;
        this.college = college;
        this.major = major;
        this.teacher = teacher;
        this.learnRequire = learnRequire;
        this.teachProgress = teachProgress;
        this.examArrange = examArrange;
    }
}
