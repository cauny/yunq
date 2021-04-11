package com.ep.yunq.dao;

import com.ep.yunq.pojo.CourseSignIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:39
  **/
public interface CourseSignInDAO extends JpaRepository<CourseSignIn,Integer> {
}
