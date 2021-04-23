package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @classname: UserInfo
 * @Author: yan
 * @Date: 2021/3/27 16:11
 * 功能描述：
 **/
@Entity
@Table(name="user_info")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String username;    //用户名
    private String realname;    //真实姓名
    private String defaultRole;    //真实姓名
    private String nickname;    //昵称
    private String ino; //学号/工号
    private Integer sex; //性别
    private String school; //学校
    private String college; //学院
    private String major; //专业
    private String avatar; //头像

    @ManyToOne()
    @JoinColumn(name = "user_id")
    User user;

    @Transient
    private List<AdminRole> roles;


    public UserInfo() {

    }

    public UserInfo(String username, User user) {
        this.username = username;
        this.user = user;
    }
}
