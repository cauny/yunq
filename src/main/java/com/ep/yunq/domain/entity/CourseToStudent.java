package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @classname: CourseToStudent
 * @Author: yan
 * @Date: 2021/4/12 20:22
 * 功能描述：
 **/
@Entity
@Table(name = "course_to_stu")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class CourseToStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 经验值
     */
    private int experience;

    private String level;

    /**
     * 学生
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User stu;

    /**
     * 课程
     */
    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;
}
