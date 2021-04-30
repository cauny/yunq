package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    /* 根据id查找用户对象 */
    User findById(int id);

    /* 根据手机号查找用户对象 */
    User findByPhone(String phone);

    @Query(value = "select new User(u.id,u.username,u.phone,u.enabled) from User u where u.enabled = 1")
    List<User> findAllByEnabled();

    /* 根据用户id删除行 */
    @Modifying
    @Transactional
    void deleteAllById(int id);

    Page<User> findAll(Pageable pageable);

    List<User> findAllByUsernameLikeOrPhoneLike(String keyword1, String keyword2);

}
