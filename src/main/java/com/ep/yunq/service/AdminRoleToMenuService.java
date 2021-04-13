package com.ep.yunq.service;

import com.ep.yunq.dao.AdminRoleToMenuDAO;
import com.ep.yunq.pojo.AdminMenu;
import com.ep.yunq.pojo.AdminRoleToMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @classname: AdminRoleToMenuService
 * @Author: yan
 * @Date: 2021/4/12 19:35
 * 功能描述：
 **/
@Service
public class AdminRoleToMenuService {

    @Autowired
    AdminRoleToMenuDAO adminRoleToMenuDAO;

    public List<AdminRoleToMenu> findAllByRoleId(int rid) {
        return adminRoleToMenuDAO.findAllByRoleId(rid);
    }

    @Modifying
    @Transactional
    public void deleteAllByRoleId(int rid) {
        adminRoleToMenuDAO.deleteAllByRoleId(rid);
    }

    @Modifying
    @Transactional
    public void deleteAllByMenuId(int mid) {
        adminRoleToMenuDAO.deleteAllByMenuId(mid);
    }

    public void save(AdminRoleToMenu rm) {
        adminRoleToMenuDAO.save(rm);
    }

    @Modifying
    @Transactional
    public String updateRoleMenu(int rid, List<AdminMenu> menus) {
        String message = "";
        try{
            deleteAllByRoleId(rid);
            for (AdminMenu menu: menus){
                AdminRoleToMenu rm = new AdminRoleToMenu();
                rm.setRoleId(rid);
                rm.setMenuId(menu.getId());
                adminRoleToMenuDAO.save(rm);
            }
            message = "更新成功";
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            message = "参数错误，更新失败";
        }

        return message;
    }
}
