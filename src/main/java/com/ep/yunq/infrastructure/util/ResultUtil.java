package com.ep.yunq.infrastructure.util;

import com.ep.yunq.domain.entity.Result;
import org.springframework.stereotype.Component;

/**
 * @classname: ResultUtil
 * @Author: yan
 * @Date: 2021/3/27 10:06
 * 功能描述：结果工具类
 **/
@Component
public class ResultUtil {

    /* 返回成功信息 */
    public static Result buildSuccessResult(Object data){  return buildResult(ConstantUtil.SUCCESS,"成功",data);}

    /* 返回失败信息 */
    public static Result buildFailResult(String message){  return buildResult(ConstantUtil.FAIL,message,null);}

    /* 返回成功信息 */
    public static Result buildExceptionResult(Object data){  return buildResult(ConstantUtil.FAIL,"出错",data);}

    /* 返回值 */
    public static Result buildResult(ConstantUtil constant,String message,Object data){
        return new Result(constant.code,message,data);
    }

}
