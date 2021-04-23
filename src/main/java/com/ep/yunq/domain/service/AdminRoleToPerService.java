package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.AdminRoleToPerDAO;
import com.ep.yunq.domain.entity.PermissionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: AdminRoleToPerService
 * @Author: yan
 * @Date: 2021/3/28 21:34
 * 功能描述：
 **/
@Service
public class AdminRoleToPerService {
    @Autowired
    AdminRoleToPerDAO adminRoleToPerDAO;

    public List<PermissionResource> findAllResByRid(int rid){   return adminRoleToPerDAO.findAllResByRid(rid);}

    public void deleteRoleResourceBind(int rid,int pid){    adminRoleToPerDAO.deleteRoleResourceBind(rid,pid);}
}
