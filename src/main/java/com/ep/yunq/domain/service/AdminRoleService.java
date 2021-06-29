package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.AdminRoleDAO;
import com.ep.yunq.domain.entity.AdminMenu;
import com.ep.yunq.domain.entity.AdminRole;
import com.ep.yunq.domain.entity.PermissionResource;
import com.ep.yunq.infrastructure.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: AdminRoleService
 * @Author: yan
 * @Date: 2021/3/25 22:05
 * 功能描述：
 **/
@Service
public class AdminRoleService {
    @Autowired
    AdminRoleDAO adminRoleDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
    @Autowired
    AdminRoleToPerService adminRoleToPerService;
    @Autowired
    AdminRoleToMenuService adminRoleToMenuService;
    @Autowired
    PermissionResourceService permissionResourceService;
    @Autowired
    AdminMenuService adminMenuService;

    /* 根据id查找 */
    public AdminRole findById(int id) {
        return adminRoleDAO.findById(id);
    }

    /* 根据角色名查找 */
    public AdminRole findByName(String name) {
        return adminRoleDAO.findByName(name);
    }

    public List<AdminRole> listIsEnabled() {
        return adminRoleDAO.findAllByEnabled();
    }

    public List<AdminRole> list() {
        List<AdminRole> roles = adminRoleDAO.findAll();
        List<PermissionResource> perms;
        List<AdminMenu> menus;
        for (AdminRole role : roles) {
            perms = permissionResourceService.listPermsByRoleId(role.getId());
            menus = PageUtil.listChange(adminMenuService.getMenusByRoleId(role.getId()),AdminMenu.class);
            role.setPerms(perms);
            role.setMenus(menus);
        }
        return roles;
    }

    /*根据用户查找角色*/
    public List<AdminRole> listRolesByUser(int uid) {
        List<AdminRole> roles = new ArrayList<>();
        List<Integer> urs = adminUserToRoleService.findRidByUid(uid);
        for (Integer ur : urs) {
            AdminRole role = findById(ur);
            if (1 == role.getEnabled())
                roles.add(role);
        }
        return roles;
    }

    /*根据用户查找角色名*/
    public List<String> listRolesNameByUser(int uid) {
        List<String> roles = new ArrayList<>();
        List<Integer> urs = adminUserToRoleService.findRidByUid(uid);
        for (Integer ur : urs) {
            AdminRole role = findById(ur);
            if (1 == role.getEnabled())
                roles.add(role.getName());
        }
        return roles;
    }

    public String add(AdminRole adminRole) {
        String message="";
        adminRole.setCreationDate(new Date());
        AdminRole adminRole1=adminRoleDAO.save(adminRole);
        message=editRoleMenuAndPerm(adminRole1.getId(), adminRole.getMenus(), adminRole.getPerms());
        if(message.equals("更新成功")){
            return "添加角色成功";
        }else {
            return "添加角色失败";
        }
    }
    public void update(AdminRole adminRole){
        adminRole.setModificationDate(new Date());
        adminRoleDAO.save(adminRole);
    }

    public String delete(int id) {
        String message = "";
        try {
            AdminRole role = adminRoleDAO.findById(id);
            if (null == role) {
                message = "角色不存在，删除失败";
            } else {
                adminRoleDAO.delete(role);
                adminRoleToPerService.deleteAllByRid(id);
                adminRoleToMenuService.deleteAllByRoleId(id);
                message = "删除成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }

        return message;
    }

    public String batchDelete(List<Integer> roleIds) {
        String message = "";
        for (int rid : roleIds) {
            message = delete(rid);
            if (!"删除成功".equals(message)) {
                break;
            }
        }
        return message;
    }

    public String edit(AdminRole requestRole) {
        String message = "";
        try {
            AdminRole adminRoleInDB = findById(requestRole.getId());
            if (null == adminRoleInDB) {
                message = "该角色不存在,修改失败";
            } else {
                if (isExist(requestRole)) {
                    message = "角色已存在，修改失败";
                } else {
                    adminRoleInDB.setName(requestRole.getName());
                    adminRoleInDB.setNameZh(requestRole.getNameZh());
                    adminRoleInDB.setModificationDate(new Date());
                    update(adminRoleInDB);
                    List<AdminMenu> menus = requestRole.getMenus();
                    List<PermissionResource> perms = requestRole.getPerms();
                    message = editRoleMenuAndPerm(requestRole.getId(), menus, perms);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，修改失败";
        }

        return message;
    }

    public String editRoleMenuAndPerm(int rid, List<AdminMenu> menus, List<PermissionResource> perms) {
        String message = adminRoleToMenuService.updateRoleMenu(rid, menus);
        if (!"更新成功".equals(message))
            return message;
        message = adminRoleToPerService.updateRolePerm(rid, perms);
        return message;
    }

    public List<AdminRole> search(String keywords) {
        return adminRoleDAO.findAllByNameLikeOrNameZhLike('%' + keywords + '%', '%' + keywords + '%');
    }


    public boolean isExist(AdminRole adminRole) {
        AdminRole roleInDB = adminRoleDAO.findByName(adminRole.getName());
        if (null == roleInDB)
            return false;
        else if (adminRole.getId() == roleInDB.getId())
            return false;
        else
            return true;
    }
}
