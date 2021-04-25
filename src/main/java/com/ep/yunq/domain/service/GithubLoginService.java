package com.ep.yunq.domain.service;

import com.ep.yunq.domain.entity.AdminUserToRole;
import com.ep.yunq.domain.entity.SysParam;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserInfo;
import com.ep.yunq.infrastructure.util.PBKDF2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @classname: GithubLoginService
 * @Author: yan
 * @Date: 2021/4/15 19:53
 * 功能描述：
 **/
@Slf4j
@Service
public class GithubLoginService {
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
    @Autowired
    SysParamService sysParamService;
    @Autowired
    PBKDF2Util pbkdf2Util;

    public String isExistUserByGithubId(int githubId){
        String message="";
        User user=new User();
        user=userService.findByGithubId(githubId);
        if(user==null){
            message="用户不存在";
            return message;
        }
        message="用户存在";
        return message;

    }

    public String bindGithubAndUser(String phone,int githubId){
        String message="";
        User user=new User();
        user=userService.findByPhone(phone);
        if(user==null){
            message="用户不存在，新建账户";
            return message;
        }
        if(user.getGithubId()!=null){
            message="用户已绑定github";
            return message;
        }
        user.setGithubId(githubId);
        userService.update(user);
        message="成功绑定用户";
        return message;

    }

    public String githubRegister(User user,String role,String avatar){
        String message="";
        try {
            int githubId=user.getGithubId();
            user.setEnabled(1);
            userService.add(user);

            //刚存进去的用户表再取出uid存入
            int uid=userService.findByGithubId(githubId).getId();
            UserInfo userInfo=new UserInfo(userService.findById(uid).getUsername(),user);
            userInfo.setDefaultRole(role);
            userInfo.setAvatar(avatar);
            userInfoService.add(userInfo);

            //设置用户角色
            int rid=adminRoleService.findByName(role).getId();
            AdminUserToRole adminUserToRole=new AdminUserToRole();
            adminUserToRole.setRoleId(rid);
            adminUserToRole.setUserId(uid);
            adminUserToRoleService.add(adminUserToRole);

            SysParam sysParam = new SysParam(new Date(), user);
            sysParamService.addOrUpdate(sysParam);

            message="注册成功";

        }catch (Exception e){
            e.printStackTrace();
            message="参数异常，注册失败";
        }
        return message;
    }

    /*用户密码电话补充*/
    public String fillPasswordAndPhone(User user){
        String message = "";
        try{
            String password = user.getPassword();
            String phone=user.getPhone();
            if (StringUtils.isEmpty(password)) {
                message = "密码为空";
                return message;
            }
            if (StringUtils.isEmpty(phone)) {
                message = "电话号码为空";
                return message;
            }

            //生成盐，默认长度16位
            String salt = pbkdf2Util.generateSalt();
            //对密码进行哈希加密
            String encodedPwd=pbkdf2Util.getEncryptedPassword(password,salt);

            //保存到用户表和用户信息表里
            user.setSalt(salt);
            user.setPassword(encodedPwd);
            userService.add(user);
            message = "重置成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，重置失败";
        }

        return message;
    }

   /* public String bindPhone(String phone){
        user

    }*/
}
