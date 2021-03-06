package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @classname: User
 * @Author: yan
 * @Date: 2021/3/25 20:16
 * 功能描述：用户实体类
 **/
@Entity
@Table(name="user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;
    private String username;    //用户名
    private String phone;   //手机号
    private String realname; //真实姓名
    private String email;   //邮箱号
    private String password;    //密码
    private String salt;    //盐
    private Integer enabled;       //是否使用

    @Transient
    private List<AdminRole> roles;

    public User(int id, String username, String realname, Integer enabled, List<AdminRole> roles) {
        this.id = id;
        this.username = username;
        this.realname = realname;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User() {

    }
}
