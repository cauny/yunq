package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @classname: AdminUserToRole
 * @Author: yan
 * @Date: 2021/3/25 20:33
 * 功能描述：用户和角色连接表
 **/
@Entity
@Table(name = "user_to_role")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class AdminUserToRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;    //用户id

    @Column(name = "role_id")
    private int roleId;    //角色id

    public AdminUserToRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public AdminUserToRole() {

    }
}
