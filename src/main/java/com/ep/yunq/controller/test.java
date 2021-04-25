package com.ep.yunq.controller;

import com.ep.yunq.application.dto.UserDTO;
import com.ep.yunq.domain.entity.AdminRole;
import com.ep.yunq.domain.entity.AdminUserToRole;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.service.AdminRoleService;
import com.ep.yunq.domain.service.AdminUserToRoleService;
import com.ep.yunq.domain.service.DictionaryDetailService;
import com.ep.yunq.domain.service.UserService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "测试")
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

    @ApiOperation("test1")
    @GetMapping("/api/test2")
    public Result pageTest(){
        User user=userService.findByPhone("13110514099");
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        return ResultUtil.buildSuccessResult(userDTO);
    }

    @ApiOperation("test3")
    @GetMapping("/api/test3")
    @RequestBody
    public Result uploadTest(HttpServletRequest request){
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        MultipartFile files = ((MultipartHttpServletRequest) request).getFile("file");
        log.info(params.getParameter("grade"));
        log.info(params.getParameter("name"));

        return ResultUtil.buildSuccessResult(files);
    }


}
