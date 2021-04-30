package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.PermissionResourceDAO;
import com.ep.yunq.domain.entity.AdminRoleToPer;
import com.ep.yunq.domain.entity.PermissionResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    AdminRoleToPerService adminRoleToPerService;

    public PermissionResource findByCode(String code) {
        return permissionResourceDAO.findByCode(code);
    }

    public PermissionResource findByName(String name) {
        return permissionResourceDAO.findByName(name);
    }

    public PermissionResource findById(int id) {
        return permissionResourceDAO.findById(id);
    }

    public List<PermissionResource> listPermsByRoleId(int rid) {
        List<AdminRoleToPer> rps = adminRoleToPerService.findAllByRoleId(rid);
        List<PermissionResource> perms = new ArrayList<>();
        for (AdminRoleToPer rp : rps) {
            PermissionResource perm = permissionResourceDAO.findById(rp.getPermissionId());
            perms.add(perm);
        }
        return perms;
    }




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


    public List<PermissionResource> search(String keywords) {
        return permissionResourceDAO.search("%" + keywords + "%");
    }

    public String add(PermissionResource adminPermission) {
        String message = "";
        try {
            permissionResourceDAO.save(adminPermission);
            message = "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，添加失败";
        }

        return message;
    }

    public String delete(Integer pid) {
        String message = "";
        try {
            permissionResourceDAO.deleteById(pid);
            adminRoleToPerService.deleteAllByPermissionId(pid);
            message = "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }

        return message;
    }

    public String batchDelete(List<Integer> permIds) {
        String message = "";
        for (int pid : permIds) {
            message = delete(pid);
            if (!"删除成功".equals(message)) {
                break;
            }
        }
        return message;
    }

    public String edit(PermissionResource adminPermission) {
        String message = "";
        try {
            permissionResourceDAO.save(adminPermission);
            message = "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，修改失败";
        }

        return message;
    }

    public List<PermissionResource> list() {
        return permissionResourceDAO.findAll();
    }

}
