package com.ep.yunq.dao;

import com.ep.yunq.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface UserDAO extends JpaRepository<User,Integer> {
    /* 根据用户名查找用户对象 */
    User findByUsername(String username);

    /* 根据手机号查找用户对象 */
    User findByPhone(String phone);

    /* 根据邮箱查找用户对象 */
    User findByEmail(String email);

    /* 根据用户名和密码查找用户对象 */
    User findByUsernameAndPassword(String username,String password);

    User findByPhoneAndUsername(String phone,String username);

    /* 根据用户id删除行 */
    @Modifying
    @Transactional
    void deleteAllByUid(int id);


}
