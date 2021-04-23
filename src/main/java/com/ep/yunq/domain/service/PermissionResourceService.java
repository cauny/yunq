package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.PermissionResourceDAO;
import com.ep.yunq.domain.entity.PermissionResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @classname: PermissionResourceService
 * @Author: yan
 * @Date: 2021/3/28 13:13
 * 功能描述：
 **/
@Slf4j
@Service
public class PermissionResourceService {

    @Autowired
    PermissionResourceDAO permissionResourceDAO;

    public PermissionResource findByCode(String code){ return permissionResourceDAO.findByCode(code);}

    public PermissionResource findByName(String name){ return permissionResourceDAO.findByName(name);}

    public PermissionResource findById(int id){ return permissionResourceDAO.findById(id);}

    public Set<String> getAllEnableResourcePath() {
        Optional<List<String>> optional = permissionResourceDAO.getEnableResourcePathRoleData();
        return optional.<Set<String>>map(HashSet::new).orElseGet(() -> new HashSet<>(0));
        /*Set<String> st = null;
        st.add("/api/v2/host===post===[role2,role3,role4]");
        return st;*/
    }

    public Set<String> getAllDisableResourcePath() {
        Optional<List<String>> optional = permissionResourceDAO.getDisableResourcePathData();
        return optional.<Set<String>>map(HashSet::new).orElseGet(() -> new HashSet<>(0));
        /*Set<String> st = null;
        st.add("/api/v2/host===post");
        return st;*/
    }


}
