package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
  * @Author: yan
  * @Date: 2021/3/27 16:15
  **/
public interface UserInfoDAO extends JpaRepository<UserInfo,Integer> {

    /* 根据用户名查找用户信息 */
    UserInfo findByUsername(String username);


    UserInfo findById(int id);

    /* 根据用户id查找用户信息 */
    @Query("from UserInfo i where i.user.id = ?1 ")
    UserInfo findByUserId(int uid);

    /* 根据用户id删除用户信息 */
    @Modifying
    @Transactional
    void deleteAllByUserId(int uid);
}
