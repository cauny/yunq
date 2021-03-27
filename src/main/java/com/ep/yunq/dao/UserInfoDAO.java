package com.ep.yunq.dao;

import com.ep.yunq.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
  * @Author: yan
  * @Date: 2021/3/27 16:15
  **/
public interface UserInfoDAO extends JpaRepository<UserInfo,Integer> {

    /* 根据用户名查找用户信息 */
    UserInfo findByUsername(String username);

    /* 根据用户id查找用户信息 */
    UserInfo findByUid(int id);

    /* 根据用户id删除用户信息 */
    @Modifying
    @Transactional
    void deleteAllByUid(int uid);
}
