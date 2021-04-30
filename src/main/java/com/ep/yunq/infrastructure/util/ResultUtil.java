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
    public static <T> Result<T> buildSuccessResult(String message,T data){
        return buildResult(ConstantUtil.SUCCESS,message,data);}

    /* 返回成功信息 */
    public static <T> Result<T> buildSuccessResult(T data){ return buildResult(ConstantUtil.SUCCESS,"成功",data);}

    /* 返回失败信息 */
    public static <T> Result<T> buildFailResult(String message){  return buildResult(ConstantUtil.FAIL,message,null);}

    /* 返回出错信息 */
    public static <T> Result<T> buildExceptionResult(T data){  return buildResult(ConstantUtil.FAIL,"出错",data);}

    /* 返回值 */
    public static <T> Result<T> buildResult(ConstantUtil constant,String message,T data){
        return new Result<T>(constant.code,message,data);
    }

    public static <T> Result<T> build(Result<T> res,T data){
        return new Result<T>(res.getCode(), res.getMessage(), data);
    }

}
