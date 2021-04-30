package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.infrastructure.util.RedisUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @classname: RedisController
 * @Author: yan
 * @Date: 2021/4/24 10:32
 * 功能描述：
 **/
@Slf4j
@RestController
public class RedisController {
    private static int ExpireTime = 120;   // redis中存储的过期时间60s

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/api/common/redis/set")
    public Result<String> redisSet(@RequestParam("key")String key, @RequestParam("value")String value){
        log.info("---------------- 获取缓存 ----------------------");
        boolean isSuccess = redisUtil.set(key, value, 20);
        if (true == isSuccess) {
            String message = "成功";
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            String message = "失败";
            return ResultUtil.buildFailResult(message);
        }
    }

    @GetMapping("/api/common/redis/get")
    public Result<Object> redisGet(@RequestParam("key")String key){
        log.info("----------------存入缓存 ----------------------");
        return ResultUtil.buildSuccessResult(redisUtil.get(key));
    }

    @GetMapping("/api/common/redis/expire")
    public Result<String> expire(@RequestParam("key")String key){
        log.info("---------------- 设置缓存时间 ----------------------");
        boolean isSuccess = redisUtil.expire(key,ExpireTime);
        if (true == isSuccess) {
            String message = "成功";
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            String message = "失败";
            return ResultUtil.buildFailResult(message);
        }
    }
}
