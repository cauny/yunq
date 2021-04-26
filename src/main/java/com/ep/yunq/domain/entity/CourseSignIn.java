package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname: CourseSignIn
 * @Author: yan
 * @Date: 2021/4/4 17:28
 * 功能描述：
 **/
@Entity
@Table(name="course_signin")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class CourseSignIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String mode;
    private Integer value;  //签到分钟数

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private Date endTime;

    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer status;

    private Integer countDown;

    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseSignIn() {
    }

    public CourseSignIn(int id, String mode, Date startTime, Date endTime) {
        this.id = id;
        this.mode = mode;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
