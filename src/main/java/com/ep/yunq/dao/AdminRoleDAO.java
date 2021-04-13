package com.ep.yunq.dao;

import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
  * @Author: yan
  * @Date: 2021/3/25 21:56
  **/
public interface AdminRoleDAO extends JpaRepository<AdminRole,Integer> {
    AdminRole findById(int id);
    AdminRole findByName(String name);
    AdminRole findByNameZh(String nameZh);

    /* 根据用户id删除行 */
    @Modifying
    @Transactional
    void deleteAllById(int id);





}
