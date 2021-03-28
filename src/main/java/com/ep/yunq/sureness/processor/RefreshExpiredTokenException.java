package com.ep.yunq.sureness.processor;

import com.usthe.sureness.processor.exception.SurenessAuthenticationException;

/**
 * @classname: RefreshExpiredTokenException
 * @Author: yan
 * @Date: 2021/3/28 11:33
 * 功能描述：
 **/
public class RefreshExpiredTokenException extends SurenessAuthenticationException {
    public RefreshExpiredTokenException(String message) {
        super(message);
    }
}
