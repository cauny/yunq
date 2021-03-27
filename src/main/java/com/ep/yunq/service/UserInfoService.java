package com.ep.yunq.service;

import com.ep.yunq.dao.UserInfoDAO;
import com.ep.yunq.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @classname: UserInfoService
 * @Author: yan
 * @Date: 2021/3/27 16:23
 * 功能描述：
 **/
@Service
public class UserInfoService {

    @Autowired
    UserInfoDAO userInfoDAO;

    /* 根据用户id查找用户信息 */
    public UserInfo findByUid(int uid){  return userInfoDAO.findByUid(uid);}

    /* 根据用户id删除用户信息 */
    public void deleteByUid(int uid){   userInfoDAO.deleteAllByUid(uid);}

    /* 对用户信息表进行添加操作 */
    public void add(UserInfo userInfo){ userInfoDAO.save(userInfo); }

}
