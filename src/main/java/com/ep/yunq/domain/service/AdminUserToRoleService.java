package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.AdminUserToRoleDAO;
import com.ep.yunq.domain.entity.AdminRole;
import com.ep.yunq.domain.entity.AdminUserToRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @classname: AdminUserToRoleService
 * @Author: yan
 * @Date: 2021/3/25 22:06
 * 功能描述：
 **/
@Slf4j
@Service
public class AdminUserToRoleService {
    @Autowired
    AdminUserToRoleDAO adminUserToRoleDAO;


    public void addOrUpdate(AdminUserToRole adminUserRole) {
        adminUserToRoleDAO.save(adminUserRole);
    }

    /* 根据用户id查找角色id */
    public List<Integer> findRidByUid(int uid){   return adminUserToRoleDAO.findRoleIdByUserId(uid);}

    /* 根据角色id查找所有用户id */
    public List<Integer> findAllUidByRid(int rid){  return adminUserToRoleDAO.findAllUserIdByRoleId(rid);}

    /* 根据用户id和角色id进行删除 */
    public void deleteByUidAndRid(int uid, int rid){ adminUserToRoleDAO.deleteByUserIdAndRoleId(uid,rid);}

    /* 根据用户id进行删除 */
    public void deleteByUid(int uid){   adminUserToRoleDAO.deleteAllByUserId(uid);}

    /* 对用户角色关系表进行更新和添加操作 */
    public void add(AdminUserToRole adminUserToRole) {
        //检查该对象是否存在，存在时比较是否一样，不一样的时删除然后添加，对象不存在直接添加
        if (findRidByUid(adminUserToRole.getUserId()).isEmpty()) {
            adminUserToRoleDAO.save(adminUserToRole);
            return;
        }
        List<Integer> rids=findRidByUid(adminUserToRole.getUserId());
        for(int r:rids){
            if(r==adminUserToRole.getRoleId()){
                return;
            }
        }
        adminUserToRoleDAO.save(adminUserToRole);

    }

    @Modifying
    @Transactional
    public String assistUser(int rid, LinkedHashMap userIds) {
        String message = "";
        try{
            Set<Integer> userIdsSetInDB = new HashSet<>(findAllUidByRid(rid));
            Set<Integer> temp = new HashSet<>();
            for (Integer uid : userIdsSetInDB){
                temp.add(uid);
            }
            Set<Integer> userIdsSet = null;
            for (Object value : userIds.values()) {
                List<Integer> userIdsList = (List<Integer>)value;
                userIdsSet = new HashSet<>(userIdsList);
                for (int i = 0; i < userIdsList.size(); i++) {
                    // 去重请求的用户和数据库的用户 剩下的为新增的和需要减少的 重复的数据不变
                    if (!temp.add(userIdsList.get(i))){
                        // 已经存在
                        userIdsSetInDB.remove(userIdsList.get(i));
                        userIdsSet.remove(userIdsList.get(i));
                    }
                }
            }
            if (!userIdsSet.isEmpty()) {
                for (Integer uid : userIdsSet) {
                    AdminUserToRole ur = new AdminUserToRole();
                    ur.setRoleId(rid);
                    ur.setUserId(uid);
                    addOrUpdate(ur);
                }
            }
            if (!userIdsSetInDB.isEmpty()) {
                for (Integer uid : userIdsSetInDB){
                    deleteByUidAndRid(rid, uid);
                }
            }
            message = "分配成功";
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            message = "参数错误，分配失败";
        }

        return message;
    }

    @Modifying
    @Transactional
    public void deleteAllByUid(int uid) {
        adminUserToRoleDAO.deleteAllByUserId(uid);
    }

    @Modifying
    @Transactional
    public String saveRoleChanges(int uid, List<AdminRole> roles) {
        String message = "";
        try{
            deleteAllByUid(uid);
            for (AdminRole role : roles) {
                AdminUserToRole adminUserRole = new AdminUserToRole();
                adminUserRole.setUserId(uid);
                adminUserRole.setRoleId(role.getId());
                addOrUpdate(adminUserRole);
            }
            message = "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            message = "参数错误，修改失败";
        }

        return message;
    }
}
