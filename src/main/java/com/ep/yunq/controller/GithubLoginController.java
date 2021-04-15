package com.ep.yunq.controller;

import com.ep.yunq.pojo.Result;
import com.ep.yunq.pojo.User;
import com.ep.yunq.pojo.UserBasicInfo;
import com.ep.yunq.pojo.UserInfo;
import com.ep.yunq.service.GithubLoginService;
import com.ep.yunq.service.UserInfoService;
import com.ep.yunq.service.UserService;
import com.ep.yunq.util.ConstantUtil;
import com.ep.yunq.util.GithubConstant;
import com.ep.yunq.util.HttpClientUtil;
import com.ep.yunq.util.ResultUtil;
import com.tencentcloudapi.asr.v20190614.models.Model;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @classname: GithubLoginController
 * @Author: yan
 * @Date: 2021/4/14 17:30
 * 功能描述：
 **/
@Api
@Slf4j
@RestController
public class GithubLoginController {
    @Autowired
    GithubLoginService githubLoginService;
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    //回调地址
    @ApiOperation("访问回调")
    @GetMapping("/callback")
    public Result callback(String code, String state, Model model, HttpServletRequest req) throws Exception{

        String message="";
        if(!StringUtils.isEmpty(code)&&!StringUtils.isEmpty(state)){
            //拿到我们的code,去请求token
            //发送一个请求到
            String token_url = GithubConstant.TOKEN_URL.replace("CODE", code);
            //得到的responseStr是一个字符串需要将它解析放到map中
            String responseStr = HttpClientUtil.doGet(token_url);
            // 调用方法从map中获得返回的--》 令牌
            String token = HttpClientUtil.getMap(responseStr).get("access_token");
            log.info(token);

            //根据token发送请求获取登录人的信息  ，通过令牌去获得用户信息
            String userinfo_url = GithubConstant.USER_INFO_URL;
            log.info(userinfo_url);
            responseStr = HttpClientUtil.doGetUser(userinfo_url,token);//json
            log.info(responseStr);

            Map<String, String> responseMap = HttpClientUtil.getMapByJson(responseStr);

            int id=Integer.parseInt(responseMap.get("id"));
            message=githubLoginService.isExistUserByGithubId(id);
            String username=responseMap.get("login");
            String avatar=responseMap.get("avatar_url");
            if(!message.equals("用户存在")){
                User user =new User();
                user.setGithubId(id);
                user.setUsername(username);
                //默认角色为学生
                message=githubLoginService.githubRegister(user,"student",avatar);
                if(!message.equals("注册成功")){
                    return ResultUtil.buildFailResult(message);
                }
                message="补充手机号和密码";
                return ResultUtil.buildResult(ConstantUtil.FAIL,message,id);
            }
            // 成功则登陆
            User user=userService.findByGithubId(id);
            Map<String, Object> responseData=userService.loginMessage(user.getPhone());
            return ResultUtil.buildSuccessResult(responseData);
        }
        message="授权登录失败";
        return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("补充电话和密码")
    @PutMapping("/api/password-phone")
    public Result fillPasswordAndPhone(@RequestParam String phone,
                                       @RequestParam int githubId,
                                       @RequestParam String verificationCode,
                                       @RequestParam String password){
        String message="";
        if(userService.findByPhone(phone)!=null){
            message="该号码已被使用";
            return ResultUtil.buildFailResult(message);
        }
        message=userService.verifyCode(phone,verificationCode);
        if(!message.equals("验证成功")){
            return ResultUtil.buildFailResult(message);
        }
        User user=new User();
        user=userService.findByGithubId(githubId);
        user.setPhone(phone);
        user.setPassword(password);
        message=userService.resetPassword(user);
        if(!message.equals("重置成功")){
            return ResultUtil.buildFailResult(message);
        }
        Map<String, Object> responseData=userService.loginMessage(phone);
        responseData.put("Message","成功绑定用户");
        return ResultUtil.buildSuccessResult(responseData);

    }
}
