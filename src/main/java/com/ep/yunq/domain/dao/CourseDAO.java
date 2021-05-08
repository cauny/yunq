package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: yan
 * @Date: 2021/4/10 22:36
 **/
public interface CourseDAO extends JpaRepository<Course, Integer> {
    Course findById(int id);

    Course findByCode(String code);

    Page<Course> findAllByCreator(int id, Pageable pageable);

    Page<Course> findAll(Pageable pageable);
    
    Page<Course> findAllByNameLikeOrTeacherLikeOrGradeLikeOrSemesterLikeOrderBySemesterAsc(String k1, String k2, String k3, String k4, Pageable pageable);
}
