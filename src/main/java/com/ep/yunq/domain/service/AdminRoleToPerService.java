package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.AdminRoleToPerDAO;
import com.ep.yunq.domain.entity.AdminRoleToPer;
import com.ep.yunq.domain.entity.PermissionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @classname: AdminRoleToPerService
 * @Author: yan
 * @Date: 2021/3/28 21:34
 * 功能描述：
 **/
@Service
public class AdminRoleToPerService {
    @Autowired
    AdminRoleToPerDAO adminRoleToPerDAO;

    public List<PermissionResource> findAllResByRid(int rid){   return adminRoleToPerDAO.findAllResByRid(rid);}

    public void deleteRoleResourceBind(int rid,int pid){    adminRoleToPerDAO.deleteRoleResourceBind(rid,pid);}

    @Modifying
    @Transactional
    public void deleteAllByRid(int rid){
        adminRoleToPerDAO.deleteAllByRoleId(rid);
    }

    @Modifying
    @Transactional
    public void deleteAllByPermissionId(int pid){
        adminRoleToPerDAO.deleteAllByPermissionId(pid);
    }

    @Modifying
    @Transactional
    public String updateRolePerm(int rid, List<PermissionResource> perms) {
        String message = "";
        try{
            if(perms.size()!=0){
                deleteAllByRid(rid);
                for (PermissionResource perm: perms){
                    AdminRoleToPer rp = new AdminRoleToPer();
                    rp.setRoleId(rid);
                    rp.setPermissionId(perm.getId());
                    adminRoleToPerDAO.save(rp);
                }
            }
            message = "更新成功";
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            message = "参数错误，更新失败";
        }

        return message;
    }

    List<AdminRoleToPer> findAllByRoleId(int rid) {
        return adminRoleToPerDAO.findAllByRoleId(rid);
    }
}
