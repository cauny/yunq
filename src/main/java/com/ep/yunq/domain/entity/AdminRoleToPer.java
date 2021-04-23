package com.ep.yunq.domain.entity;

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

    @Column(name = "permission_id")
    private int permissionId;    //权限资源id

    @Column(name = "role_id")
    private int roleId;    //角色id
}
