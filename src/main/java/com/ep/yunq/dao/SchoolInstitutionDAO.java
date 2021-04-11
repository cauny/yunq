package com.ep.yunq.dao;

import com.ep.yunq.pojo.SchoolInstitution;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:40
  **/
public interface SchoolInstitutionDAO extends JpaRepository<SchoolInstitution,Integer> {
}
