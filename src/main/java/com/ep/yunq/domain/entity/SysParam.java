package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @classname: SysParam
 * @Author: yan
 * @Date: 2021/4/9 21:07
 * 功能描述：
 **/
@Entity
@Table(name="sys_param")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class SysParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    private String signinExperience;
    private String signinRange;
    private String classTime;

    @Column(name = "level_1")
    private String level1;

    @Column(name = "level_2")
    private String level2;

    @Column(name = "level_3")
    private String level3;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 用户ID
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    User user;

    public SysParam(Date updateTime, User user) {
        this.signinExperience = "2";
        this.signinRange = "20";
        this.classTime = "45";
        this.level1="90";
        this.level2="75";
        this.level3="60";
        this.updateTime = updateTime;
        this.user = user;
    }

    public SysParam() {

    }

    public int getUserId() {
        return user.getId();
    }

    public String getUserUsername() {
        return user.getUsername();
    }

    public Integer getUserEnabled() {
        return user.getEnabled();
    }
}
