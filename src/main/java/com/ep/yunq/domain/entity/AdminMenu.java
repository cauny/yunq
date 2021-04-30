package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @classname: AdminMenu
 * @Author: yan
 * @Date: 2021/4/10 21:20
 * 功能描述：
 **/
@Entity
@Table(name = "menu")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class AdminMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单名字
     */
    private String name;

    /**
     * 菜单中文名
     */
    private String nameZh;

    private int sort;

    private int type;

    private int enabled;

    private String permission;

    /**
     * 菜单icon名
     */
    private String icon;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 父菜单
     */
    private int parentId;

    /**
     * 额外参数储存子菜单
     */
    @Transient
    private List<AdminMenu> children;
}
