package com.ep.yunq.domain.entity;

import lombok.Data;

/**
 * @classname: Result
 * @Author: yan
 * @Date: 2021/3/27 10:03
 * 功能描述：返回结果类
 **/

@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }
}
