package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @classname: AdminRoleToMenu
 * @Author: yan
 * @Date: 2021/4/10 22:12
 * 功能描述：
 **/
@Entity
@Table(name = "role_to_menu")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class AdminRoleToMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "role_id")
    private int roleId;    //角色id

    @Column(name = "menu_id")
    private int menuId;    //角色id
}
