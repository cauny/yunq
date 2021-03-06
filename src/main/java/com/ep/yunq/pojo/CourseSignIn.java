package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
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
    private String value;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private Date endTime;

    private String longitude;
    private String latitude;

    @ManyToOne()
    @JoinColumn(name = "cid")
    private Course course;
}
