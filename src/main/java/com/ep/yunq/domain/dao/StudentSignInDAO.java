package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.StudentSignIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:37
  **/
public interface StudentSignInDAO extends JpaRepository<StudentSignIn,Integer> {
    @Query(value = "from StudentSignIn where courseSignIn.id = ?1 and studentId =?2")
    StudentSignIn findByCourseSignInAndStudent(int cid, int uid);

    @Query(value = "from StudentSignIn  where studentId = ?1 and courseSignIn.course.id = ?2 ")
    List<StudentSignIn> findAllByStudentAndCourseId(int uid, int cid);

    @Query(nativeQuery = true, value = "select ss.time, ss.mode ,ui.ino, ui.name from student_signin ss " +
            " left join `user_info` ui on ui.user_id= ss.student_id " +
            " where ss.course_signin_id = ?1 order by id asc")
    List<Map<String,Object>> findAllByCourseSignIn(int csiid);
}
