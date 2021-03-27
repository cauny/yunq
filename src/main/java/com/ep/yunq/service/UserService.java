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
    public boolean isExist(String username) {
        User user = findByUserName(username);
        return null!=user;
    }

    /* 根据用户名查找用户 */
    public User findByUserName(String username) {
        return userDAO.findByUsername(username);
    }

    /* 根据用户名和密码查找用户 */
    public User findByUsernameAndPassword(String username,String password) {
        return userDAO.findByUsernameAndPassword(username,password);
    }

    /* 根据用户id删除用户 */
    public void deleteByUid(int id){ userDAO.deleteAllByUid(id);}

    /* 对用户表表进行添加操作 */
    public void add(User user) {  userDAO.save(user); }

    /* 用户注册 */
    public String register(User user){
        String message="";
        try {
            String username=user.getUsername();
            String password=user.getPassword();

            user.setEnabled(true);

            if(username.equals("") || password.equals("")){
                message="";
                return message;
            }
            if(!isExist(username)){
                message="";
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

            UserInfo userInfo=new UserInfo(username);
            userInfoService.add(userInfo);

            message="注册成功";

        }catch (Exception e){
            e.printStackTrace();
            message="参数异常，注册失败";
        }
        return message;
    }
}
