package com.ep.yunq.sureness.provider;

import com.ep.yunq.domain.service.UserService;
import com.usthe.sureness.provider.SurenessAccount;
import com.usthe.sureness.provider.SurenessAccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @classname: DatabaseAccountProvider
 * @Author: yan
 * @Date: 2021/3/28 11:34
 * 功能描述：
 **/
@Component
public class DatabaseAccountProvider implements SurenessAccountProvider {
    @Autowired
    UserService userService;

    @Override
    public SurenessAccount loadAccount(String appId) {
        return userService.loadAccount(Integer.parseInt(appId));
    }
}
