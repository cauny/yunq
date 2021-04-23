package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.SchoolInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:40
  **/
public interface SchoolInstitutionDAO extends JpaRepository<SchoolInstitution,Integer> {

    /* 通过id寻找机构名称 */
    SchoolInstitution findById(int id);

    /* 根据父亲id查找等级 */
    @Query(nativeQuery = true, value = "select level from school_institution where id = ?1 ")
    String findLevelById(int id);

    /* 模糊查找 */
    List<SchoolInstitution> findAllByNameLike(String keywords);

    /* 根据父id查找所有子机构 */
    List<SchoolInstitution> findAllByParentId(int parentId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from school_institution where level like ?1 ")
    int deleteAllByLevelLike(String level);


}
