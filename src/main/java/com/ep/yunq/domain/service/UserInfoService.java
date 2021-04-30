package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.UserInfoDAO;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserBasicInfo;
import com.ep.yunq.domain.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @classname: UserInfoService
 * @Author: yan
 * @Date: 2021/3/27 16:23
 * 功能描述：
 **/
@Service
@Slf4j
public class UserInfoService {

    @Autowired
    UserInfoDAO userInfoDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;

    /* 根据用户id查找用户信息 */
    public UserInfo findByUid(int uid){  return userInfoDAO.findByUserId(uid);}

    /* 根据用户id删除用户信息 */
    public void deleteByUid(int uid){   userInfoDAO.deleteAllByUserId(uid);}

    /* 对用户信息表进行添加操作 */
    public void addOrUpdate(UserInfo userInfo){ userInfoDAO.save(userInfo); }

    public UserBasicInfo createByUser(User user){
        UserInfo userInfo=findByUid(user.getId());
        UserBasicInfo userBasicInfo=new UserBasicInfo(user.getId(),userInfo.getUsername(),
                user.getPhone(),userInfo.getAvatar(),adminRoleService.listRolesNameByUser(user.getId()),
                userInfo.getDefaultRole()
        );
        return userBasicInfo;
    }

    public String edit(UserInfo userInfo) {
        String message = "";
        try {
            UserInfo userInfoInDB = userInfoDAO.findById(userInfo.getId());
            if (null == userInfoInDB) {
                message = "找不到该用户信息，修改失败";
            } else {
                User user = userInfoInDB.getUser();
                user.setUsername(userInfo.getUsername());
                userService.add(user);

                userInfoInDB.setNickname(userInfo.getNickname());
                userInfoInDB.setIno(userInfo.getIno());
                userInfoInDB.setSex(userInfo.getSex());
                userInfoInDB.setSchool(userInfo.getSchool());
                userInfoInDB.setCollege(userInfo.getCollege());
                userInfoInDB.setMajor(userInfo.getMajor());
                userInfoInDB.setUsername(userInfo.getUsername());
                /*userInfoInDB.setSchoolId(userInfo.getSchoolId());
                userInfoInDB.setCollegeId(userInfo.getCollegeId());*/
                addOrUpdate(userInfoInDB);

                //修改角色
                message = adminUserToRoleService.saveRoleChanges(user.getId(), userInfo.getRoles());

            }
        } catch (Exception e){
            message = "参数错误，修改失败";
            e.printStackTrace();
        }

        return message;
    }

}
