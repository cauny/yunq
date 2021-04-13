package com.ep.yunq.dao;

import com.ep.yunq.pojo.Course;
import com.ep.yunq.pojo.CourseToStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CourseToStudentDAO extends JpaRepository<CourseToStudent,Integer> {
    @Query(nativeQuery = true, value = "select cs.id, cs.experience, ui.ino, ui.name,ui.cover,cs.stu_id " +
            "from course_to_stu cs left join user_info ui on ui.user_id = cs.user_id where cs.course_id = ?1 ")
    List<Map<String,String>> findAllUserByCourseId(int cid);

    @Query(value = "select cs.course from  CourseToStudent cs where cs.stu.id = ?1 ")
    List<Course> findCourseByUserId(int uid);

    @Query(value = "from  CourseToStudent cs where cs.course.id = ?1 and cs.stu.id = ?2 ")
    CourseToStudent findByCourseAndUser(int cid, int uid);
}
