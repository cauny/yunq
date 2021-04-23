package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:32
  **/
public interface AdminMenuDAO extends JpaRepository<AdminMenu,Integer> {
    AdminMenu findById(int id);

    List<AdminMenu> findAllByParentId(int parentId);

    @Query(nativeQuery = true, value = "select * from admin_menu where" +
            " name like ?1 or path like ?1 or name_zh like ?1 or component like ?1 ")
    List<AdminMenu> search(String keyword1);

    void deleteById(Integer id);

    @Query(nativeQuery = true, value = "select * from admin_menu order by parent_id ")
    List<AdminMenu> findAllOrderByParentId();

    void deleteAllByParentId(int parentId);
}
