package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.SysParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysParamDAO extends JpaRepository<SysParam,Integer> {

    SysParam findById(int id);

    // List<SysParam> findAllByKey1Like(String keyword1);



    @Query(nativeQuery = true, value = "select * from sys_param sp " +
            " left join user u on u.id = sp.uid where u.name like ?1 or u.username like?1 ")
    List<SysParam> search(String keyword1);

    @Query(nativeQuery = true, value = "select * from sys_param  " +
            " where user_id = ?1 ")
    List<SysParam> findByUserId(int uid);

    Page<SysParam> findAll(Pageable pageable);
}
