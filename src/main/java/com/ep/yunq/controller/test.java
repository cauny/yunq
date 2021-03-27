package com.ep.yunq.controller;

import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.AdminUserToRole;
import com.ep.yunq.service.AdminRoleService;
import com.ep.yunq.service.AdminUserToRoleService;
import com.ep.yunq.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKeyFactory;

@Slf4j
@RestController
public class test {
    @Autowired
    UserService userService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;

    Logger logger= LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello")
    public String index(){
        String a="yan";
        AdminRole role;

        adminUserToRoleService.addAndUpdate(new AdminUserToRole(1,1));
//        logger.info("123245:{}",a);
        log.info("465754");
//        adminUserToRoleService.findRidByUid(9);
//        log.info(String.valueOf(adminUserToRoleService.findRidByUid(1)));
        return "adminRoleSe";
    }


}
