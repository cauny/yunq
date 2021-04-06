package com.ep.yunq.service;

import com.ep.yunq.dao.AdminUserToRoleDAO;
import com.ep.yunq.pojo.AdminUserToRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: AdminUserToRoleService
 * @Author: yan
 * @Date: 2021/3/25 22:06
 * 功能描述：
 **/
@Service
public class AdminUserToRoleService {
    @Autowired
    AdminUserToRoleDAO adminUserToRoleDAO;

    /* 根据用户id查找角色id */
    public List<Integer> findRidByUid(int uid){   return adminUserToRoleDAO.findRidByUid(uid);}

    /* 根据角色id查找所有用户id */
    public List<Integer> findAllUidByRid(int rid){  return adminUserToRoleDAO.findAllUidByRid(rid);}

    /* 根据用户id和角色id进行删除 */
    public void deleteByUidAndRid(int uid, int rid){ adminUserToRoleDAO.deleteByUidAndRid(uid,rid);}

    /* 根据用户id进行删除 */
    public void deleteByUid(int uid){   adminUserToRoleDAO.deleteAllByUid(uid);}

    /* 对用户角色关系表进行更新和添加操作 */
    public void addAndUpdate(AdminUserToRole adminUserToRole) {
        //检查该对象是否存在，存在时比较是否一样，不一样的时删除然后添加，对象不存在直接添加
        if (findRidByUid(adminUserToRole.getUid()) == null) {
            adminUserToRoleDAO.save(adminUserToRole);
            return;
        }
        List<Integer> rids=findRidByUid(adminUserToRole.getUid());
        for(int r:rids){
            if(r!=adminUserToRole.getRid()){
                deleteByUid(adminUserToRole.getUid());
                adminUserToRoleDAO.save(adminUserToRole);
                return;
            }

        }

    }
}
