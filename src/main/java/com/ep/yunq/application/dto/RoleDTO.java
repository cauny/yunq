package com.ep.yunq.application.dto;

import com.ep.yunq.domain.entity.AdminMenu;
import com.ep.yunq.domain.entity.PermissionResource;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @classname: RoleDTO
 * @Author: yan
 * @Date: 2021/4/24 12:02
 * 功能描述：
 **/
@Data
public class RoleDTO {
    private Integer id;
    private String name;
    private String nameZh;
    private Integer enabled;
    private List<PermissionResource> perms;

    private List<AdminMenu> menus;
}
