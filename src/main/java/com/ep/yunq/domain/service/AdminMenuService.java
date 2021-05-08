package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.MenuDTO;
import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.domain.dao.AdminMenuDAO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.infrastructure.util.PageUtil;
import org.apache.shiro.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<MenuDTO> getMenusByUserId(int uid) {
        User user = userService.findById(uid);
        List<AdminRole> roleList = adminRoleService.listRolesByUser(user.getId());
        List<AdminMenu> menus = new ArrayList<>();
        for (AdminRole role : roleList) {
            List<AdminRoleToMenu> rms = adminRoleToMenuService.findAllByRoleId(role.getId());
            for (AdminRoleToMenu rm : rms) {
                // 防止多角色状态下菜单重复的逻辑
                AdminMenu menu = adminMenuDAO.findById(rm.getMenuId());
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

    public List<MenuDTO> getMenusByRoleId(int rid) {
        List<AdminMenu> menus = new ArrayList<>();
        List<AdminRoleToMenu> rms = adminRoleToMenuService.findAllByRoleId(rid);
        for (AdminRoleToMenu rm : rms) {
            AdminMenu menu = adminMenuDAO.findById(rm.getMenuId());
            menu.setChildren(null);
            menus.add(menu);
        }
        return handleMenus(menus);

    }
    public List<MenuDTO> list() {
        List<AdminMenu> menus = adminMenuDAO.findAll();
        return handleMenus(menus);
    }

    public List<MenuDTO> handleMenus(List<AdminMenu> menus) {
        List<MenuDTO> menuDTOS=PageUtil.listChange(menus,MenuDTO.class);
        List<MenuDTO> deleteMenus = new ArrayList<>();
        for (MenuDTO menu : menuDTOS) {
            menu.setHasChildren(false);
            for (MenuDTO menu2 : menuDTOS) {
                if (menu.getId() == menu2.getParentId()) {
                    menu.setHasChildren(true);
                    if (null == menu.getChildren()) {
                        List<MenuDTO> children = new ArrayList<>();
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
        for (MenuDTO menu : deleteMenus) {
            menuDTOS.remove(menu);
        }

        return menuDTOS;
    }

    public String add(AdminMenu adminMenu) {
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

    public String delete(Integer mid) {
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

    public String batchDelete(List<Integer> menuIds) {
        String message = "";
        for (int mid : menuIds) {
            message = delete(mid);
            if (!"删除成功".equals(message)) {
                break;
            }
        }
        return message;
    }

    public String edit(AdminMenu adminMenu) {
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

    public List<MenuDTO> search(String keywords) {
        List<AdminMenu> menus = adminMenuDAO.search("%" + keywords + "%");
        /*List<AdminMenu> deleteMenus = new ArrayList<AdminMenu>();
        for (AdminMenu adminMenu : menus) {
            for (AdminMenu menu : menus) {
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
        for (AdminMenu adminMenu : deleteMenus) {
            menus.remove(adminMenu);
        }*/
        return handleMenus(menus);
    }

    public List<MenuDTO> all() {
        List<AdminMenu> menus = adminMenuDAO.findAllOrderByParentId();
        return handleMenus(menus);
    }

    public List<MenuDTO> findAllByParentId(int mid) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        List<AdminMenu> adminMenus=adminMenuDAO.findAllByParentId(mid,sort);
        List<MenuDTO> menuDTOS= PageUtil.listChange(adminMenus,MenuDTO.class);
        for(MenuDTO si:menuDTOS){
            si.setChildren(findAllByParentId(si.getId()));
            if(findAllByParentId(si.getId()).size()==0){
                si.setHasChildren(false);
            }else{
                si.setHasChildren(true);
            }
        }
        return menuDTOS;
    }

    public String findFather(int mid){
        int parentId;
        parentId=findById(mid).getParentId();
        if(parentId==0){
            return "无父节点";
        }
        return findById(parentId).getNameZh();
    }




}
