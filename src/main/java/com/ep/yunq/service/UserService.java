package com.ep.yunq.service;

import com.ep.yunq.dao.UserDAO;
import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.User;
import com.ep.yunq.pojo.UserInfo;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserInfoService userInfoService;


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

    /* 用户注册 */
    public String register(User user){
        String message="";
        try {
            String username=user.getUsername();
            String password=user.getPassword();
            String phone = user.getPhone();
            String email = user.getEmail();

            user.setEnabled(true);

            if(username.equals("") || password.equals("")){
                message="";
                return message;
            }
            if(!isExistByUsername(username)){
                message="用户名已被注册";
                return message;
            }
            if(!isExistByPhone(phone)){
                message="手机号已被注册";
                return message;
            }
            if(!isExistByEmail(email)){
                message="邮箱已被注册";
                return message;
            }

            //生成盐，默认长度16位
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            //设置哈希迭代次数
            int times=5;
            //对密码进行哈希加密
            String encodedPwd=new SimpleHash("md5",password,salt,times).toString();

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
}
