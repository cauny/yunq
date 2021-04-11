package com.ep.yunq.dao;

import com.ep.yunq.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:36
  **/
public interface CourseDAO extends JpaRepository<Course,Integer> {
}
