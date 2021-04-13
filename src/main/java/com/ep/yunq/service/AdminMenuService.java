package com.ep.yunq.service;

import com.ep.yunq.dao.AdminMenuDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @classname: AdminMenuService
 * @Author: yan
 * @Date: 2021/4/12 19:32
 * 功能描述：
 **/
@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserToRoleService adminUserToRoleService;
}
