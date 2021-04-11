package com.ep.yunq.dao;

import com.ep.yunq.pojo.StudentSignIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:37
  **/
public interface StudentSignInDAO extends JpaRepository<StudentSignIn,Integer> {
}
