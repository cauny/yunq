package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.AdminRoleToMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:34
  **/
public interface AdminRoleToMenuDAO extends JpaRepository<AdminRoleToMenu,Integer> {
    AdminRoleToMenu findById(int id);

    List<AdminRoleToMenu> findAllByRoleId(int roleId);

    void deleteAllByRoleId(int roleId);

    void deleteAllByMenuId(int menuId);
}
