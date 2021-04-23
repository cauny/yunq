package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname: StudentSignIn
 * @Author: yan
 * @Date: 2021/4/4 21:45
 * 功能描述： 学生签到
 **/
@Entity
@Table(name="student_signin")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class StudentSignIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String mode;    //
    private Integer value;   //

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;  //

    private BigDecimal longitude;   //
    private BigDecimal latitude;    //
    private Integer studentId;    //

    private float distance;

    @ManyToOne()
    @JoinColumn(name = "course_signin_id")
    private CourseSignIn courseSignIn;  //

}
