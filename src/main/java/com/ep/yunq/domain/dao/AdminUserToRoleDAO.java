package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.AdminUserToRole;
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
    @Query(nativeQuery = true,value = "select role_id from user_to_role where user_id= ?1")
    List<Integer> findRoleIdByUserId(int uid);

    /* 根据角色id查找所有用户id */
    @Query(nativeQuery = true,value = "select user_id from admin_user_role where role_id = ?1")
    List<Integer> findAllUserIdByRoleId(int rid);

    /* 根据用户id删除行 */
    @Modifying
    @Transactional
    void deleteAllByUserId(int uid);

    /* 根据用户id和角色id删除行 */
    @Modifying
    @Transactional
    void deleteByUserIdAndRoleId(int uid, int rid);



}
