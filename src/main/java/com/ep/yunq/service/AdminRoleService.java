package com.ep.yunq.service;

import com.ep.yunq.dao.AdminRoleDAO;
import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.AdminUserToRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /* 根据id查找 */
    public AdminRole findById(int id){  return adminRoleDAO.findById(id);}

    /* 根据角色名查找 */
    public AdminRole findByName(String name){  return adminRoleDAO.findByName(name);}

    public void deleteByUid(int id){ adminRoleDAO.deleteAllById(id);}

    /* 对角色表进行更新和添加操作 */
    public void addAndUpdate(AdminRole adminRole) {
        //检查该对象是否存在，存在时比较是否一样，不一样的时删除然后添加，对象不存在直接添加
        if (findByName(adminRole.getName()) == null) {
            adminRoleDAO.save(adminRole);
        } else if(findByName(adminRole.getName()).getEnabled()!=adminRole.getEnabled()){
            deleteByUid(adminRole.getId());
            adminRoleDAO.save(adminRole);
        }
    }


}
