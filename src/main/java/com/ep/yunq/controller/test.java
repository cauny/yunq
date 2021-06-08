package com.ep.yunq.controller;

import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.application.dto.UserLoginDTO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.domain.service.*;
import com.ep.yunq.infrastructure.util.PBKDF2Util;
import com.ep.yunq.infrastructure.util.PageUtil;
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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    UserAuthsService userAuthsService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    PBKDF2Util pbkdf2Util;
    @Autowired
    SchoolInstitutionService schoolInstitution;


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

    @ApiOperation("te")
    @CrossOrigin
    @GetMapping("/api/test")
    public Result tes(){
        return ResultUtil.buildSuccessResult(userService.findById(93));
    }


    @ApiOperation("test1")
    @GetMapping("/api/test2")
    public Result pageTest(){
        User user=userService.findByPhone("13506976031");
        UserInfo userInfo=userInfoService.findByUid(user.getId());
        ModelMapper modelMapper = new ModelMapper();
        log.info("role:",user.getRoles());
//        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        UserLoginDTO userLoginDTO =modelMapper.map(userInfo, UserLoginDTO.class);
        return ResultUtil.buildSuccessResult(userLoginDTO);
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

    @ApiOperation("test4")
    @GetMapping("/api/test4")
    @RequestBody
    public Result est(@RequestBody UserAuths userAuths){
        UserAuths userAuths1= userAuthsService.add(userAuths);

        return ResultUtil.buildSuccessResult(userAuths1);
    }

    @ApiOperation("test5")
    @GetMapping("/api/test5")
    @RequestBody
    public Page<User> est5(@RequestParam int pageNum,@RequestParam int pageSize){

        List<User> users=userService.listIsEnabled();
        Page<User> us= PageUtil.listToPage(users,pageNum,pageSize);
        return us;
    }

    @ApiOperation("test6")
    @GetMapping("/api/test6")
    @RequestBody
    public Result est(@RequestBody SchoolInstitutionDTO schoolInstitutionDTO){

        ModelMapper modelMapper = new ModelMapper();
        SchoolInstitution schoolInstitution=modelMapper.map(schoolInstitutionDTO,SchoolInstitution.class);
        return ResultUtil.buildSuccessResult(schoolInstitution);
    }

    @ApiOperation("test7")
    @GetMapping("/api/test7")
    @RequestBody
    public Result<List<SchoolInstitutionDTO>> rrest(@RequestParam Integer id){
        List<Integer> ids=new ArrayList<>();
        ids.add(id);
        List<SchoolInstitutionDTO> tmp=schoolInstitution.findFa(ids);
        return ResultUtil.buildSuccessResult(tmp);
    }





}
