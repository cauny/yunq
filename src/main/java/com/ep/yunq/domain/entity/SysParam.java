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

    private String name;
    private String nameZh;
    private String value;


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


    public SysParam(String name, String nameZh, String value, Date updateTime, User user) {
        this.name = name;
        this.nameZh = nameZh;
        this.value = value;
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
