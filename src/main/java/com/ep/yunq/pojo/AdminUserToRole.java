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

    private int uid;    //用户id
    private int rid;    //角色id

    public AdminUserToRole(int uid, int rid) {
        this.uid = uid;
        this.rid = rid;
    }

    public AdminUserToRole() {

    }
}
