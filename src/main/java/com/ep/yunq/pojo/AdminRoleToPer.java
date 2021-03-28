package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @classname: AdminRoleToPer
 * @Author: yan
 * @Date: 2021/3/28 21:25
 * 功能描述：
 **/
@Entity
@Table(name = "role_to_permission")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class AdminRoleToPer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int pid;    //权限资源id
    private int rid;    //角色id
}
