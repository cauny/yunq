package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.UserInfoDAO;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserBasicInfo;
import com.ep.yunq.domain.entity.UserInfo;
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
    @Autowired
    AdminRoleService adminRoleService;

    /* 根据用户id查找用户信息 */
    public UserInfo findByUid(int uid){  return userInfoDAO.findByUserId(uid);}

    /* 根据用户id删除用户信息 */
    public void deleteByUid(int uid){   userInfoDAO.deleteAllByUserId(uid);}

    /* 对用户信息表进行添加操作 */
    public void add(UserInfo userInfo){ userInfoDAO.save(userInfo); }

    public UserBasicInfo createByUser(User user){
        UserInfo userInfo=findByUid(user.getId());
        UserBasicInfo userBasicInfo=new UserBasicInfo(userInfo.getUsername(),
                user.getPhone(),userInfo.getAvatar(),adminRoleService.listRolesNameByUser(user.getId()),
                userInfo.getDefaultRole()
        );
        return userBasicInfo;
    }

}
