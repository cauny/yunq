package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @classname: AdminRole
 * @Author: yan
 * @Date: 2021/3/25 20:30
 * 功能描述：后台管理系统中的角色
 **/

@Entity
@Table(name="role")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class AdminRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;
    private String nameZh;
    private Integer enabled;

    @Transient
    private List<PermissionResource> perms;

    @Transient
    private List<AdminMenu> menus;

    public AdminRole() {
    }

    public AdminRole(String name, Integer enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}
