package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.UserAuthsDAO;
import com.ep.yunq.domain.entity.UserAuths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @classname: UserAuthsService
 * @Author: yan
 * @Date: 2021/4/26 17:01
 * 功能描述：
 **/
@Service
public class UserAuthsService {
    @Autowired
    UserAuthsDAO userAuthsDAO;

    public UserAuths findById(int id) {
        return userAuthsDAO.findById(id);
    }

    public UserAuths findByIdentityTypeAndIdentifier(String identityType, String identifier) {
        return userAuthsDAO.findByIdentityTypeAndIdentifier(identityType, identifier);
    }

    public UserAuths add(UserAuths userAuths) { return userAuthsDAO.save(userAuths); }

}
