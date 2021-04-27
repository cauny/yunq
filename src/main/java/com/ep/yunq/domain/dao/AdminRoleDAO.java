package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.AdminRole;
import com.ep.yunq.domain.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: yan
 * @Date: 2021/3/25 21:56
 **/
public interface AdminRoleDAO extends JpaRepository<AdminRole, Integer> {
    AdminRole findById(int id);

    AdminRole findByName(String name);

    AdminRole findByNameZh(String nameZh);

    List<AdminRole> findAllByNameLikeOrNameZhLike(String keyword1, String keyword2);

    /* 根据用户id删除行 */
    @Modifying
    @Transactional
    void deleteAllById(int id);

    Page<AdminRole> findAll(Pageable pageable);

}
