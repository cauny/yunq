package com.ep.yunq.application.dto;

import com.ep.yunq.domain.entity.AdminMenu;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @classname: MenuDTO
 * @Author: yan
 * @Date: 2021/4/28 10:56
 * 功能描述：
 **/
@Data
public class MenuDTO {
    private int id;
    private String path;
    private String name;
    private String nameZh;
    private int sort;
    private int type;
    private int enabled;
    private String permission;
    private String icon;
    private String component;
    private int parentId;
    private Boolean hasChildren;
    private List<MenuDTO> children;

}
