package com.ep.yunq.service;

import com.ep.yunq.dao.UserDAO;
import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.User;
import com.ep.yunq.pojo.UserInfo;
import com.ep.yunq.util.PBKDF2Util;
import com.usthe.sureness.provider.DefaultAccount;
import com.usthe.sureness.provider.SurenessAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
    @Autowired
    PBKDF2Util pbkdf2Util;



    /* 根据用户名判断用户是否存在 */
    public boolean isExistByUsername(String username) {
        User user = findByUserName(username);
        return null!=user;
    }

    /* 根据电话判断用户是否存在 */
    public boolean isExistByPhone(String phone) {
        User user = findByPhone(phone);
        return null!=user;
    }

    /* 根据邮箱判断用户是否存在 */
    public boolean isExistByEmail(String email) {
        User user = findByEmail(email);
        return null!=user;
    }



    /* 根据用户名查找用户 */
    public User findByUserName(String username) {
        return userDAO.findByUsername(username);
    }


    /* 根据手机号查找用户 */
    public User findByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    /* 根据用户名查找用户 */
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    /* 根据用户名和密码查找用户 */
    public User findByUsernameAndPassword(String username,String password) {
        return userDAO.findByUsernameAndPassword(username,password);
    }

    /* 根据用户id删除用户 */
    public void deleteByUid(int id){ userDAO.deleteAllById(id);}

    /* 对用户表表进行添加操作 */
    public void add(User user) {  userDAO.save(user); }

    /* 根据用户id查找对象 */
    public AdminRole findRoleById(int id){ return adminRoleService.findById(adminUserToRoleService.findRidByUid(id));}

    /* 用户注册 */
    public String register(User user){
        String message="";
        try {
            String username=user.getUsername();
            String password=user.getPassword();
            String phone = user.getPhone();
            String email = user.getEmail();

            user.setEnabled(1);

            if(username.equals("") || password.equals("")){
                message="";
                return message;
            }
            if(isExistByUsername(username)){
                message="用户名已被注册";
                return message;
            }
            if(isExistByPhone(phone)){
                message="手机号已被注册";
                return message;
            }
            if(isExistByEmail(email)){
                message="邮箱已被注册";
                return message;
            }

            //生成盐，默认长度16位
            String salt = pbkdf2Util.generateSalt();
            //对密码进行哈希加密
            String encodedPwd=pbkdf2Util.getEncryptedPassword(password,salt);

            //保存到用户表和用户信息表里
            user.setSalt(salt);
            user.setPassword(encodedPwd);
            add(user);

            //刚存进去的用户表再取出uid存入
            Integer uid=findByUserName(username).getId();
            UserInfo userInfo=new UserInfo(username,uid);
            userInfoService.add(userInfo);

            message="注册成功";

        }catch (Exception e){
            e.printStackTrace();
            message="参数异常，注册失败";
        }
        return message;
    }

    /* 用户登录验证 */
    public String authUser(String username,String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User authuser=findByUserName(username);
        if(authuser==null){     return "用户不存在";}
        if(password==null){    return "密码为空";}

        password=pbkdf2Util.getEncryptedPassword(password,authuser.getSalt());
        if(authuser.getPassword().equals(password)) {
            return "登录成功";
        }else {
            return "密码错误";
        }

    }

    public SurenessAccount loadAccount(String username){
        log.info("11111");
        User authUserOptional = findByUserName(username);
        if (authUserOptional!=null) {
            User authUser = authUserOptional;
            DefaultAccount.Builder accountBuilder = DefaultAccount.builder(username)
                    .setPassword(authUser.getPassword())
                    .setSalt(authUser.getSalt())
                    .setDisabledAccount(1 != authUser.getEnabled())
                    .setExcessiveAttempts(2 == authUser.getEnabled());
            AdminRole role = findRoleById(findByUserName(username).getId());
            List<String> roles = null;
            roles.add(role.getName());

            if (role != null) {
                accountBuilder.setOwnRoles(roles);
            }
            return accountBuilder.build();
        } else {
            return null;
        }

    }

}
