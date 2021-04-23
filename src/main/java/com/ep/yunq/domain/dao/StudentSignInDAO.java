package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.StudentSignIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:37
  **/
public interface StudentSignInDAO extends JpaRepository<StudentSignIn,Integer> {
}
