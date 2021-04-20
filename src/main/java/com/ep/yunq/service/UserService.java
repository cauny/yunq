package com.ep.yunq.service;

import com.ep.yunq.dao.UserDAO;
import com.ep.yunq.pojo.*;
import com.ep.yunq.util.ConstantUtil;
import com.ep.yunq.util.PBKDF2Util;
import com.ep.yunq.util.RedisUtil;
import com.ep.yunq.util.ResultUtil;
import com.usthe.sureness.provider.DefaultAccount;
import com.usthe.sureness.provider.SurenessAccount;
import com.usthe.sureness.util.JsonWebTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

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
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SysParamService sysParamService;


    public Page<User> userPage(int pageNumber, int pageSize){
        Sort sort=Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> res=userDAO.findAll(pageable);
        return res;

    }

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

    /* 根据用户名查找用户 */
    public User findById(int id) {
        return userDAO.findById(id);
    }

    /* 根据手机号查找用户 */
    public User findByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    /* 根据用户名查找用户 */
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    /*  */
    public User findByGithubId(int id){return userDAO.findByGithubId(id);}

    /* 根据用户id查找用户角色 */
    public List<AdminRole> findRoleByUserId(int uid) {
        return adminRoleService.listRolesByUser(uid);
    }

    /* 根据用户名和密码查找用户 */
    public User findByUsernameAndPassword(String username,String password) {
        return userDAO.findByUsernameAndPassword(username,password);
    }

    /* 根据用户id删除用户 */
    public void deleteByUid(int id){ userDAO.deleteAllById(id);}

    /* 对用户表表进行添加操作 */
    public void add(User user) {  userDAO.save(user); }

    /* 对用户表表进行更新操作 */
    public void update(User user) {
        deleteByUid(findByPhone(user.getPhone()).getId());
        add(user);
    }

    /* 根据用户id查找角色名称 */
    public List<String> findRolesById(int id){
        List<Integer> rids=adminUserToRoleService.findRidByUid(id);
        List<String> roles = new ArrayList<>();
        for(int r:rids){
            roles.add(adminRoleService.findById(r).getName());
        }

        return roles;}

    /* 用户注册 */
    public String register(User user,String role){
        String message="";
        try {
            String username=user.getUsername();
            String password=user.getPassword();
            String phone = user.getPhone();

            user.setEnabled(1);

            if(username.equals("") || password.equals("")){
                message="用户名或密码为空，注册失败";
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
//            if(isExistByEmail(email)){
//                message="邮箱已被注册";
//                return message;
//            }

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
            UserInfo userInfo=new UserInfo(username,user);
            userInfo.setDefaultRole(role);
            userInfoService.add(userInfo);

            //设置用户角色
            int rid=adminRoleService.findByName(role).getId();
            AdminUserToRole adminUserToRole=new AdminUserToRole();
            log.info(String.valueOf(rid));
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

    /*用户密码重置*/
    public String resetPassword(User user){
        String message = "";
        try{
            String password = user.getPassword();
            if (StringUtils.isEmpty(password)) {
                message = "密码为空，重置失败";
                return message;
            }
            User userInDB = userDAO.findByPhone(user.getPhone());
            if (null == userInDB) {
                message = "未找到该用户";
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
            message = "重置成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，重置失败";
        }

        return message;
    }

    /* 用户密码登录验证 */
    public String authUser(String username,String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User authuser=findByUserName(username);
        if(authuser==null){    return "用户不存在";}
        if(password==null){    return "密码为空";}

        password=pbkdf2Util.getEncryptedPassword(password,authuser.getSalt());
        if(authuser.getPassword().equals(password)) {
            return "登录成功";
        }else {
            return "密码错误";
        }

    }

    /* 用户验证码登录验证 */
    public String authUserByVerificationCode(String phone,String verificationCode) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User authuser=findByPhone(phone);
        if(authuser==null){    return "用户不存在";}
        if(verificationCode==null){    return "验证码为空";}

        String message=verifyCode(phone,verificationCode);
        if(!message.equals("验证成功")){
            return message;
        }else{
            return "验证成功";
        }

    }

    /*发送验证码*/
    public String sendCode(){
        int a=(int)((Math.random()*9+1)*1000);
        String code= String.valueOf(a);
        return code;
    }

    /*验证码验证*/
    public String verifyCode(String phone,String code){
        Object redisVerificationCode = redisUtil.get(phone + ConstantUtil.SMS_Verification_Code.code);
        log.info(code);
        if (ObjectUtils.isEmpty(redisVerificationCode)) {
            String message = "验证码超时,请重新获取";
            return message;
        } else if (!redisVerificationCode.equals(code)) {
            String message = "验证码错误";
            return message;
        }else {
            String message = "验证成功";
            return message;
        }
    }

    /* 使用token */
    public String useToken(User user){
        //获取角色信息
        List<String> roles=findRolesById(findByUserName(user.getUsername()).getId());

        //刷新时间5小时
        long refreshPeriodTime = 36000L;
        String jwt = JsonWebTokenUtil.issueJwt(UUID.randomUUID().toString(), user.getUsername(),
                "tom-auth-server", refreshPeriodTime >> 1, roles,
                null, false);
        String responseData = jwt;
        return responseData;
    }
    /*public Map<String, String> useToken(User user){
        //获取角色信息
        List<String> roles=findRolesById(findByUserName(user.getUsername()).getId());

        //刷新时间5小时
        long refreshPeriodTime = 36000L;
        String jwt = JsonWebTokenUtil.issueJwt(UUID.randomUUID().toString(), user.getUsername(),
                "tom-auth-server", refreshPeriodTime >> 1, roles,
                null, false);
        Map<String, String> responseData = Collections.singletonMap("token", jwt);
        return responseData;
    }*/

    /* 登录后信息返回 */
    public Map<String, Object> loginMessage(String phone){
        User user=new User();
        user=findByPhone(phone);
        UserBasicInfo userBasicInfo= userInfoService.createByUser(user);

        String token= useToken(user);
        Map<String, Object> responseData= new HashMap<>(Collections.singletonMap("token", token));
        responseData.put("userInfo",userBasicInfo);
        return responseData;
    }

    public SurenessAccount loadAccount(String username){
        User authUserOptional = findByUserName(username);
        if (authUserOptional!=null) {
            User authUser = authUserOptional;
            DefaultAccount.Builder accountBuilder = DefaultAccount.builder(username)
                    .setPassword(authUser.getPassword())
                    .setSalt(authUser.getSalt())
                    .setDisabledAccount(1 != authUser.getEnabled())
                    .setExcessiveAttempts(2 == authUser.getEnabled());
            List<String> roles = findRolesById(findByUserName(username).getId());

            if (roles != null) {
                accountBuilder.setOwnRoles(roles);
            }
            return accountBuilder.build();
        } else {
            return null;
        }

    }

}
