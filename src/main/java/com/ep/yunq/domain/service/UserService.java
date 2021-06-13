package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.UserDTO;
import com.ep.yunq.domain.entity.UserBasicInfo;
import com.ep.yunq.application.dto.UserLoginDTO;
import com.ep.yunq.domain.dao.UserDAO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.PBKDF2Util;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.RedisUtil;
import com.usthe.sureness.provider.DefaultAccount;
import com.usthe.sureness.provider.SurenessAccount;
import com.usthe.sureness.util.JsonWebTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    public List<User> list() {
        List<User> users = userDAO.findAll();
        users=addRoles(users);
        return users;
    }

    public List<User> listIsEnabled() {
        List<User> users = userDAO.findAllByEnabled();
        users=addRoles(users);
        return users;
    }

    /* 根据电话判断用户是否存在 */
    public boolean isExistByPhone(String phone) {
        User user = findByPhone(phone);
        return null != user;
    }

    /* 根据用户名查找用户 */
    public User findById(int id) {
        return userDAO.findById(id);
    }

    /* 根据手机号查找用户 */
    public User findByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    /* 根据用户id查找用户角色 */
    public List<AdminRole> findRoleByUserId(int uid) {
        return adminRoleService.listRolesByUser(uid);
    }

    /* 对用户表表进行更新操作 */
    public void update(User user) {
        user.setModificationDate(new Date());
        userDAO.save(user);
    }

    /* 对用户表表进行添加操作 */
    public User addAndReturn(User user) {
        user.setCreationDate(new Date());
        return userDAO.save(user);
    }

    /* 根据用户id查找角色名称 */
    public List<String> findRolesById(int id) {
        List<Integer> rids = adminUserToRoleService.findRidByUid(id);
        List<String> roles = new ArrayList<>();
        for (int r : rids) {
            roles.add(adminRoleService.findById(r).getName());
        }

        return roles;
    }

    public String createUser(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String message="";
        String username = user.getUsername();
        String password = user.getPassword();
        String phone=user.getPhone();
        if (phone!=null&&isExistByPhone(phone)) {
            message = "手机号已被注册";
            return message;
        }
        if(password!=null){
            //生成盐，默认长度16位
            String salt = pbkdf2Util.generateSalt();
            //对密码进行哈希加密
            String encodedPwd = pbkdf2Util.getEncryptedPassword(password, salt);

            //保存到用户表和用户信息表里
            user.setSalt(salt);
            user.setPassword(encodedPwd);
        }
        user.setEnabled(1);
        user = addAndReturn(user);

        List<AdminRole> roles=user.getRoles();

        //新建userInfo
        UserInfo userInfo = new UserInfo(username, user);
        userInfo.setDefaultRole(roles.get(0).getName());
        userInfoService.addOrUpdate(userInfo);

        //设置用户角色
        for(AdminRole role:roles){
            int rid = role.getId();
            AdminUserToRole adminUserToRole = new AdminUserToRole();
            log.info(String.valueOf(rid));
            adminUserToRole.setRoleId(rid);
            adminUserToRole.setUserId(user.getId());
            adminUserToRoleService.add(adminUserToRole);
        }

        sysParamService.createUserSysParam(user);
        message="创建成功";
        return message;
    }

    /* 用户注册 */
    public String register(User user, String role) {
        String message = "";
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            if (username.equals("") || password.equals("")) {
                message = "用户名或密码为空，注册失败";
                return message;
            }
            user.setCreator(user.getId());
            List<AdminRole> roles=new ArrayList<>();
            roles.add(adminRoleService.findByName(role));
            user.setRoles(roles);
            message= createUser(user);
            if(message.equals("创建成功")){
                message = "注册成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，注册失败";
        }
        return message;
    }

    /* 用户注册 */
    public String registerByAdmin(User user,Integer creatorId) {
        String message = "";
        try {
            user.setCreator(creatorId);
            message= createUser(user);
            if(message.equals("创建成功")){
                message = "注册成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，注册失败";
        }
        return message;
    }

    /*用户密码重置*/
    public String resetPassword(User user) {
        String message = "";
        try {
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
            String encodedPwd = pbkdf2Util.getEncryptedPassword(password, salt);

            //保存到用户表和用户信息表里
            userInDB.setSalt(salt);
            userInDB.setPassword(encodedPwd);
            update(userInDB);
            message = "重置成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，重置失败";
        }

        return message;
    }

    /* 电话密码登录验证 */
    public String authUser(String phone, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User authuser = findByPhone(phone);
        if (authuser == null) {
            return "用户不存在";
        }
        if (password == null) {
            return "密码为空";
        }
        if (authuser.getEnabled() == 0) {
            return "该用户已禁用";
        }

        password = pbkdf2Util.getEncryptedPassword(password, authuser.getSalt());
        if (authuser.getPassword().equals(password)) {
            return "登录成功";
        } else {
            return "密码错误";
        }

    }

    /*发送验证码*/
    public String sendCode() {
        int a = (int) ((Math.random() * 9 + 1) * 1000);
        String code = String.valueOf(a);
        return code;
    }

    /*验证码验证*/
    public String verifyCode(String phone, String code) {
        Object redisVerificationCode = redisUtil.get(phone + ConstantUtil.SMS_Verification_Code.code);
        log.info(code);
        if (ObjectUtils.isEmpty(redisVerificationCode)) {
            String message = "验证码超时,请重新获取";
            return message;
        } else if (!redisVerificationCode.equals(code)) {
            String message = "验证码错误";
            return message;
        } else {
            redisUtil.del(phone + ConstantUtil.SMS_Verification_Code.code);
            String message = "验证成功";
            return message;
        }
    }

    /* 使用token */
    public String useToken(User user) {
        //获取角色信息
        List<String> roles = findRolesById(user.getId());

        //刷新时间5小时
        long refreshPeriodTime = 36000L;
        String jwt = JsonWebTokenUtil.issueJwt(UUID.randomUUID().toString(), String.valueOf(user.getId()),
                "tom-auth-server", refreshPeriodTime >> 1, roles,
                null, false);
        String responseData = jwt;
        return responseData;
    }

    /* 登录后信息返回 */
    public UserLoginDTO loginMessage(String phone) {
        User user = new User();
        user = findByPhone(phone);
        UserBasicInfo userBasicInfo = userInfoService.createByUser(user);

        String token = useToken(user);
        UserLoginDTO userLoginDTO =new UserLoginDTO(userBasicInfo,token);
        return userLoginDTO;
    }

    public UserLoginDTO loginMessageByUserId(int id) {
        User user = findById(id);
        if(user.getEnabled()==0){
            return null;
        }
        UserBasicInfo userBasicInfo = userInfoService.createByUser(user);

        String token = useToken(user);
        UserLoginDTO userLoginDTO =new UserLoginDTO(userBasicInfo,token);
        return userLoginDTO;
    }

    public SurenessAccount loadAccount(int id) {
        User authUserOptional = findById(id);
        if (authUserOptional != null) {
            User authUser = authUserOptional;
            DefaultAccount.Builder accountBuilder = DefaultAccount.builder(authUser.getUsername())
                    .setPassword(authUser.getPassword())
                    .setSalt(authUser.getSalt())
                    .setDisabledAccount(1 != authUser.getEnabled())
                    .setExcessiveAttempts(2 == authUser.getEnabled());
            List<String> roles = findRolesById(id);

            if (roles != null) {
                accountBuilder.setOwnRoles(roles);
            }
            return accountBuilder.build();
        } else {
            return null;
        }

    }

    public String delete(int uid) {
        String message = "";
        try{
            User u = userDAO.findById(uid);
            if (null == u){
                message = "用户不存在，删除失败";
            } else {
                userDAO.delete(u);
                message = "删除成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }
        return message;
    }

    public String batchDelete(List<Integer> userIds) {
        String message = "";
        for (int uid : userIds) {
            message = delete(uid);
            if (!"删除成功".equals(message)) {
                break;
            }
        }
        return message;
    }

    public String editUser(UserDTO user,int modifierId) {
        String message = "";
        try {
            User userInDB = findById(user.getId());
            if (null == userInDB) {
                log.info("111111");
                message = "找不到该用户，修改失败";
                return message;
            }
            userInDB.setUsername(user.getUsername());
            userInDB.setEnabled(user.getEnabled());
            userInDB.setPhone(user.getPhone());
            userInDB.setModifier(modifierId);
            update(userInDB);
            //如果传入角色list为null，则不修改
            if(user.getRoles()!=null){
                message = adminUserToRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
            }
            message="修改成功";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            message = "参数异常，修改失败";
        }

        return message;
    }

    public List<User> search(String keywords) {
        List<User> us = userDAO.findAllByUsernameLikeOrPhoneLike('%' + keywords + '%', '%' + keywords + '%');
        List<User> users=addRoles(us);
        return users;
    }

    public List<User> addRoles(List<User> users){
        List<AdminRole> roles;
        for (User u : users) {
            roles = adminRoleService.listRolesByUser(u.getId());
            u.setRoles(roles);
        }
        return users;
    }

}
