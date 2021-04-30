package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.UserDTO;
import com.ep.yunq.domain.entity.UserBasicInfo;
import com.ep.yunq.application.dto.UserLoginDTO;
import com.ep.yunq.domain.dao.UserDAO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.PBKDF2Util;
import com.ep.yunq.infrastructure.util.RedisUtil;
import com.usthe.sureness.provider.DefaultAccount;
import com.usthe.sureness.provider.SurenessAccount;
import com.usthe.sureness.util.JsonWebTokenUtil;
import lombok.extern.slf4j.Slf4j;
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


    public Page<User> list(int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> res = userDAO.findAll(pageable);
        return res;

    }

    public List<User> listIsEnabled() {
        List<User> users = userDAO.findAllByEnabled();
        List<AdminRole> roles;
        for (User user : users) {
            roles = adminRoleService.listRolesByUser(user.getId());
            user.setRoles(roles);
        }
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

    /* 根据用户id删除用户 */
    public void deleteByUid(int id) {
        userDAO.deleteAllById(id);
    }

    /* 对用户表表进行添加操作 */
    public void add(User user) {
        userDAO.save(user);
    }

    /* 对用户表表进行添加操作 */
    public User addAndReturn(User user) {
        return userDAO.save(user);
    }

    /* 对用户表表进行更新操作 */
    public void update(User user) {
        add(user);
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

    /* 用户注册 */
    public String register(User user, String role) {
        String message = "";
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            String phone = user.getPhone();

            user.setEnabled(1);

            if (username.equals("") || password.equals("")) {
                message = "用户名或密码为空，注册失败";
                return message;
            }
            if (isExistByPhone(phone)) {
                message = "手机号已被注册";
                return message;
            }
            //生成盐，默认长度16位
            String salt = pbkdf2Util.generateSalt();
            //对密码进行哈希加密
            String encodedPwd = pbkdf2Util.getEncryptedPassword(password, salt);

            //保存到用户表和用户信息表里
            user.setSalt(salt);
            user.setPassword(encodedPwd);
            user = addAndReturn(user);

            //刚存进去的用户表再取出uid存入
            Integer uid = user.getId();
            UserInfo userInfo = new UserInfo(username, user);
            userInfo.setDefaultRole(role);
            userInfoService.addOrUpdate(userInfo);

            //设置用户角色
            int rid = adminRoleService.findByName(role).getId();
            AdminUserToRole adminUserToRole = new AdminUserToRole();
            log.info(String.valueOf(rid));
            adminUserToRole.setRoleId(rid);
            adminUserToRole.setUserId(uid);
            adminUserToRoleService.add(adminUserToRole);

            SysParam sysParam = new SysParam(new Date(), user);
            sysParamService.addOrUpdate(sysParam);

            message = "注册成功";

        } catch (Exception e) {
            e.printStackTrace();
            message = "参数异常，注册失败";
        }
        return message;
    }

    /* 用户注册 */
    public String registerByAdmin(User user, String role) {
        String message = "";
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            String phone = user.getPhone();

            user.setEnabled(1);
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
            user = addAndReturn(user);

            //刚存进去的用户表再取出uid存入
            Integer uid = user.getId();
            UserInfo userInfo = new UserInfo(username, user);
            userInfo.setDefaultRole(role);
            userInfoService.addOrUpdate(userInfo);

            //设置用户角色
            int rid = adminRoleService.findByName(role).getId();
            AdminUserToRole adminUserToRole = new AdminUserToRole();
            log.info(String.valueOf(rid));
            adminUserToRole.setRoleId(rid);
            adminUserToRole.setUserId(uid);
            adminUserToRoleService.add(adminUserToRole);

            SysParam sysParam = new SysParam(new Date(), user);
            sysParamService.addOrUpdate(sysParam);

            message = "注册成功";

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
            add(userInDB);
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

        password = pbkdf2Util.getEncryptedPassword(password, authuser.getSalt());
        if (authuser.getPassword().equals(password)) {
            return "登录成功";
        } else {
            return "密码错误";
        }

    }

    /* 用户验证码登录验证 */
    public String authUserByVerificationCode(String phone, String verificationCode) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User authuser = findByPhone(phone);
        if (authuser == null) {
            return "用户不存在";
        }
        if (verificationCode == null) {
            return "验证码为空";
        }

        String message = verifyCode(phone, verificationCode);
        if (!message.equals("验证成功")) {
            return message;
        } else {
            return "验证成功";
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
    public UserLoginDTO loginMessage(String phone) {
        User user = new User();
        user = findByPhone(phone);
        UserBasicInfo userBasicInfo = userInfoService.createByUser(user);

        String token = useToken(user);
        UserLoginDTO userLoginDTO =new UserLoginDTO();
        userLoginDTO.setUserInfo(userBasicInfo);
        userLoginDTO.setToken(token);
        return userLoginDTO;
    }

    public Map<String, Object> loginMessageByUserId(int id) {
        User user = new User();
        user = findById(id);
        UserBasicInfo userBasicInfo = userInfoService.createByUser(user);

        String token = useToken(user);
        Map<String, Object> responseData = new HashMap<>(Collections.singletonMap("token", token));
        responseData.put("userInfo", userBasicInfo);
        return responseData;
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

    public String editUser(UserDTO user) {
        String message = "";
        try {
            User userInDB = userDAO.findById(user.getId());
            if (null == userInDB) {
                message = "找不到该用户，修改失败";
                return message;
            }
            userInDB.setUsername(user.getUsername());
            userInDB.setPhone(user.getPhone());

            add(userInDB);
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
        log.info("搜索2"+us);
        List<AdminRole> roles;
        for (User u : us) {
            u.setPassword("");
            u.setSalt("");

            roles = adminRoleService.listRolesByUser(u.getId());
            u.setRoles(roles);
        }
        return us;
    }



}
