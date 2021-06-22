package com.ep.yunq.domain.service;

import com.ep.yunq.domain.entity.*;
import com.ep.yunq.infrastructure.util.PBKDF2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    UserAuthsService userAuthsService;

    public String isExistUserByGithubId(int githubId){
        String message="";
        UserAuths userAuths=new UserAuths();
        userAuths=userAuthsService.findByIdentityTypeAndIdentifier("github",String.valueOf(githubId));
        if(userAuths==null){
            message="用户不存在";
            return message;
        }
        message="用户存在";
        return message;

    }

    public String githubRegister(User user,String role,String avatar,UserAuths userAuths){
        String message="";
        try {
            /*UserInfo userInfo=new UserInfo(user);
            userInfo.setDefaultRole(role);
            if(userInfo.getAvatar()==null){
                userInfo.setAvatar(avatar);
            }
            userInfoService.addOrUpdate(userInfo);

            //设置用户角色
            int rid=adminRoleService.findByName(role).getId();
            AdminUserToRole adminUserToRole=new AdminUserToRole();
            adminUserToRole.setRoleId(rid);
            adminUserToRole.setUserId(user.getId());
            adminUserToRoleService.add(adminUserToRole);

            SysParam sysParam = new SysParam(new Date(), user);
            sysParamService.addOrUpdate(sysParam);*/
            List<AdminRole> roles=new ArrayList<>();
            roles.add(adminRoleService.findByName(role));
            user.setRoles(roles);
            message=userService.createUser(user,null);
            if(message.equals("创建成功")){
                message = "注册成功";
            }
            userAuths.setUserId(user.getId());
            userAuthsService.add(userAuths);
            return message;

        }catch (Exception e){
            e.printStackTrace();
            message="参数异常，注册失败";
            return message;
        }
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
            userService.update(user);
            message = "重置成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，重置失败";
        }
        return message;
    }
}
