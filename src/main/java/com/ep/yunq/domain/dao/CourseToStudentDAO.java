package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.CourseToStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CourseToStudentDAO extends JpaRepository<CourseToStudent,Integer> {
    @Query(nativeQuery = true, value = "select cs.id, cs.experience, ui.ino, ui.username,cs.user_id " +
            "from course_to_stu cs left join user_info ui on ui.user_id = cs.user_id where cs.course_id = ?1 ")
    List<Map<String,String>> findAllUserByCourseId(int cid);

    @Query(value = "select cs.course from  CourseToStudent cs where cs.stu.id = ?1 ")
    List<Course> findCourseByUserId(int uid);

    @Query(value = "from  CourseToStudent cs where cs.course.code = ?1 and cs.stu.id = ?2 ")
    CourseToStudent findByCourseCodeAndUser(String code, int uid);

    @Query(value = "from  CourseToStudent cs where cs.course.id = ?1 and cs.stu.id = ?2 ")
    CourseToStudent findByCourseAndUser(int cid, int uid);
}
