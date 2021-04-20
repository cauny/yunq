package com.ep.yunq.controller;

import com.ep.yunq.dto.UserDTO;
import com.ep.yunq.pojo.*;
import com.ep.yunq.service.AdminRoleService;
import com.ep.yunq.service.AdminUserToRoleService;
import com.ep.yunq.service.DictionaryDetailService;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKeyFactory;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    DictionaryDetailService dictionaryDetailService;

    Logger logger= LoggerFactory.getLogger(getClass());

    @CrossOrigin
    @GetMapping("/api/index")
    public Result index(){
        String a="yan";
        AdminRole role;

        adminUserToRoleService.add(new AdminUserToRole(1,1));
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

    @GetMapping("/api/list")
    public Result pageTest(@RequestParam int pageNum,@RequestParam int pageSize){
        Page<User> res= userService.userPage(pageNum,pageSize);
        return ResultUtil.buildSuccessResult(res);
    }

    @GetMapping("/api/test2")
    public Result pageTest(){
        User user=userService.findByPhone("13110514099");
        ModelMapper modelMapper = new ModelMapper();
        List<User> users=new ArrayList<>();
        users.add(user);
        List<UserDTO> userDTOS=modelMapper.map(users,new TypeToken<List<UserDTO>>() {}.getType());
//        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        return ResultUtil.buildSuccessResult(userDTOS);
    }


}
