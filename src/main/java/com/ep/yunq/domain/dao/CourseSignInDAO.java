package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.CourseSignIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:39
  **/
public interface CourseSignInDAO extends JpaRepository<CourseSignIn,Integer> {
}
