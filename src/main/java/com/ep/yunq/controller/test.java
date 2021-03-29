package com.ep.yunq.controller;

import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.AdminUserToRole;
import com.ep.yunq.pojo.Result;
import com.ep.yunq.service.AdminRoleService;
import com.ep.yunq.service.AdminUserToRoleService;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKeyFactory;

@Slf4j
@RestController
@CrossOrigin
public class test {
    @Autowired
    UserService userService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;

    Logger logger= LoggerFactory.getLogger(getClass());

    @CrossOrigin
    @RequestMapping("/api/index")
    public Result index(){
        String a="yan";
        AdminRole role;

        adminUserToRoleService.addAndUpdate(new AdminUserToRole(1,1));
//        logger.info("123245:{}",a);
        log.info("465754");
//        adminUserToRoleService.findRidByUid(9);
//        log.info(String.valueOf(adminUserToRoleService.findRidByUid(1)));
        return ResultUtil.buildSuccessResult("哈哈哈哈哈哈哈哈哈哈哈");
    }

    @CrossOrigin
    @RequestMapping("/api/test")
    public Result tes(){
        return ResultUtil.buildSuccessResult("这是测试界面");
    }


}
