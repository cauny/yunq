package com.ep.yunq.dao;

import com.ep.yunq.pojo.AdminMenu;
import com.ep.yunq.pojo.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:32
  **/
public interface AdminMenuDAO extends JpaRepository<AdminMenu,Integer> {
    AdminMenu findById(int id);
}
