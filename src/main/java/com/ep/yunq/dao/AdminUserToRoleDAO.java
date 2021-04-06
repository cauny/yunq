package com.ep.yunq.dao;

import com.ep.yunq.pojo.AdminUserToRole;
import com.ep.yunq.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
  * @Author: yan
  * @Date: 2021/3/25 21:58
  **/
public interface AdminUserToRoleDAO extends JpaRepository<AdminUserToRole,Integer> {
    AdminUserToRole findById(int id);

    /* 根据用户id查找角色id */
    @Query(nativeQuery = true,value = "select rid from user_to_role where uid= ?1")
    List<Integer> findRidByUid(int uid);

    /* 根据角色id查找所有用户id */
    @Query(nativeQuery = true,value = "select uid from admin_user_role where rid = ?1")
    List<Integer> findAllUidByRid(int rid);

    /* 根据用户id删除行 */
    @Modifying
    @Transactional
    void deleteAllByUid(int uid);

    /* 根据用户id和角色id删除行 */
    @Modifying
    @Transactional
    void deleteByUidAndRid(int uid, int rid);



}
