package com.ep.yunq.controller;

import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.application.dto.UserLoginDTO;
import com.ep.yunq.domain.entity.*;
import com.ep.yunq.domain.service.*;
import com.ep.yunq.infrastructure.util.PBKDF2Util;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import com.usthe.sureness.subject.SubjectSum;
import com.usthe.sureness.subject.support.JwtSubject;
import com.usthe.sureness.util.SurenessContextHolder;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;

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


    LocalDateTime localDateTime = LocalDateTime.of(2021, 06, 22, 16, 50, 01);
    @GetMapping("/api/test/demo")
    public void demo(){

        /**
         * localDateTime.get(ChronoField.YEAR); 获取年
         * localDateTime.get(ChronoField.MONTH_OF_YEAR); 获取月
         * localDateTime.get(ChronoField.DAY_OF_MONTH);  获取日
         * localDateTime.get(ChronoField.HOUR_OF_DAY); 获取小时
         * localDateTime.get(ChronoField.MINUTE_OF_HOUR); 获取分
         * localDateTime.get(ChronoField.SECOND_OF_MINUTE); 获取秒
         */

        System.out.println(
                localDateTime.get(ChronoField.YEAR)+":"
                        +localDateTime.get(ChronoField.MONTH_OF_YEAR)+":"
                        +localDateTime.get(ChronoField.DAY_OF_MONTH)+":"
                        +localDateTime.get(ChronoField.HOUR_OF_DAY)+":"
                        +localDateTime.get(ChronoField.MINUTE_OF_HOUR)+":"
                        +localDateTime.get(ChronoField.SECOND_OF_MINUTE)
        );

        Calendar calendar = Calendar.getInstance();

        /**
         * 指定触发的时间
         * */
        calendar.set(Calendar.YEAR, localDateTime.get(ChronoField.YEAR)); // 设置年份
        calendar.set(Calendar.DAY_OF_MONTH, localDateTime.get(ChronoField.DAY_OF_MONTH));//设置日期
        calendar.set(Calendar.MONTH, localDateTime.get(ChronoField.MONTH_OF_YEAR)-1);//设置日期为月份   这里3表示4月份    4就表示5月份
        calendar.set(Calendar.HOUR_OF_DAY, localDateTime.get(ChronoField.HOUR_OF_DAY)); //设置触发时
        calendar.set(Calendar.MINUTE, localDateTime.get(ChronoField.MINUTE_OF_HOUR)); //设置触发分
        calendar.set(Calendar.SECOND, localDateTime.get(ChronoField.SECOND_OF_MINUTE)); //设置触发秒

        Date time = calendar.getTime();
        Timer timer = new Timer();
        timer.schedule(new RemindTask(), time);
    }
    class RemindTask extends TimerTask {

        public void run() {
            System.out.println("localDateTime:"+localDateTime);
        }
    }


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
        SubjectSum subject = SurenessContextHolder.getBindSubject();
        if (subject == null || subject.getPrincipal() == null) {
            return ResultUtil.buildFailResult("111");
        }
        String appId = (String) subject.getPrincipal();
        return ResultUtil.buildSuccessResult(appId);
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

        log.info("11111");
        return ResultUtil.buildSuccessResult(userAuths1);
    }

    @ApiOperation("test5")
    @GetMapping("/api/test5")
    @RequestBody
    public Page<User> est5(@RequestParam int pageNum,@RequestParam int pageSize){

        log.info("11111");
        List<User> users=userService.listIsEnabled();
        Page<User> us= PageUtil.listToPage(users,pageNum,pageSize);
        return us;
    }

    @ApiOperation("test6")
    @GetMapping("/api/test6")
    @RequestBody
    public Result est(@RequestBody SchoolInstitutionDTO schoolInstitutionDTO){

        log.info("11111");
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
        log.info("11111");
        List<SchoolInstitutionDTO> tmp=schoolInstitution.findFa(ids);
        return ResultUtil.buildSuccessResult(tmp);
    }





}
