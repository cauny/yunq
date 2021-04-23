package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.AdminMenuDAO;
import com.ep.yunq.domain.entity.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: AdminMenuService
 * @Author: yan
 * @Date: 2021/4/12 19:32
 * 功能描述：
 **/
@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminRoleToMenuService adminRoleToMenuService;

    public AdminMenu findById(int id) {
        return adminMenuDAO.findById(id);
    }

    public List<AdminMenu> getMenusByUserId(int uid){
        User user=userService.findById(uid);
        List<AdminRole> roleList = adminRoleService.listRolesByUser(user.getId());
        List<AdminMenu> menus = new ArrayList<>();
        for(AdminRole role:roleList){
            List<AdminRoleToMenu> rms = adminRoleToMenuService.findAllByRoleId(role.getId());
            for(AdminRoleToMenu rm:rms){
                // 防止多角色状态下菜单重复的逻辑
                AdminMenu menu=adminMenuDAO.findById(rm.getMenuId());
                boolean isExist = false;
                for (AdminMenu m : menus) {
                    if (m.getId() == menu.getId()) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    menus.add(menu);
                }
            }

        }
        return handleMenus(menus);

    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<AdminMenu> menus = new ArrayList<>();
        List<AdminRoleToMenu> rms = adminRoleToMenuService.findAllByRoleId(rid);
        for (AdminRoleToMenu rm : rms) {
            AdminMenu menu =adminMenuDAO.findById(rm.getMenuId());
            menu.setChildren(null);
            menus.add(menu);
        }
        return handleMenus(menus);

    }

    public List<AdminMenu> handleMenus(List<AdminMenu>menus) {
        List<AdminMenu> deleteMenus = new ArrayList<>();
        for (AdminMenu menu: menus){
            for (AdminMenu menu2: menus) {
                if (menu.getId() == menu2.getParentId()) {
                    if (null == menu.getChildren()) {
                        List<AdminMenu> children = new ArrayList<>();
                        children.add(menu2);
                        menu.setChildren(children);
                        deleteMenus.add(menu2);
                    } else {
                        menu.getChildren().add(menu2);
                        deleteMenus.add(menu2);
                    }
                }
            }
        }
        for (AdminMenu menu: deleteMenus) {
            menus.remove(menu);
        }
        return menus;
    }

    public String add(AdminMenu adminMenu){
        String message = "";
        try {
            adminMenuDAO.save(adminMenu);
            message = "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，添加失败";
        }

        return message;
    }

    public String delete(Integer mid){
        String message = "";
        try {
            adminMenuDAO.deleteById(mid);
            adminMenuDAO.deleteAllByParentId(mid);
            adminRoleToMenuService.deleteAllByMenuId(mid);
            message = "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }

        return message;
    }

    public String batchDelete(LinkedHashMap menuIds) {
        String message = "";
        for (Object value : menuIds.values()) {
            for (int mid :(List<Integer>)value) {
                message = delete(mid);
                if (!"删除成功".equals(message)) {
                    break;
                }
            }
        }
        return message;
    }

    public String edit(AdminMenu adminMenu){
        String message = "";
        try {
            adminMenuDAO.save(adminMenu);
            message = "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，修改失败";
        }

        return message;
    }

    public List<AdminMenu> search(String keywords) {
        List<AdminMenu> menus =adminMenuDAO.search("%" + keywords + "%");
        List<AdminMenu> deleteMenus = new ArrayList<AdminMenu>();
        for (AdminMenu adminMenu: menus){
            for (AdminMenu menu: menus) {
                if (adminMenu.getId() == menu.getParentId()) {
                    if (null == adminMenu.getChildren()) {
                        List<AdminMenu> children = new ArrayList<>();
                        children.add(menu);
                        adminMenu.setChildren(children);
                        deleteMenus.add(menu);
                    } else {
                        adminMenu.getChildren().add(menu);
                        deleteMenus.add(menu);
                    }
                }
            }
        }
        for (AdminMenu adminMenu:deleteMenus) {
            menus.remove(adminMenu);
        }
        return menus;
    }

    public List<AdminMenu> all() {
        List<AdminMenu> menus = adminMenuDAO.findAllOrderByParentId();
        return handleMenus(menus);
    }


}
