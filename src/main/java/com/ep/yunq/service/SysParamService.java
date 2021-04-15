package com.ep.yunq.service;

import com.ep.yunq.dao.SysParamDAO;
import com.ep.yunq.pojo.AdminRole;
import com.ep.yunq.pojo.SysParam;
import com.ep.yunq.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @classname: SysParamService
 * @Author: yan
 * @Date: 2021/4/9 21:12
 * 功能描述：
 **/
@Service
public class SysParamService {
    @Autowired
    SysParamDAO sysParamDAO;
    @Autowired
    AdminRoleService adminRoleService;

    public void addOrUpdate(SysParam sysParam) {
        sysParamDAO.save(sysParam);
    }

    public String add(SysParam sysParam) {
        String message = "";
        try {
            sysParam.setUpdateTime(new Date());
            addOrUpdate(sysParam);
            message = "添加成功";
        } catch (Exception e) {
            message = "参数异常，添加失败";
            e.printStackTrace();
        }

        return message;
    }

    public String edit(SysParam sysParam) {
        String message = "";
        try {
            SysParam sysParamInDB = sysParamDAO.findById(sysParam.getId());
            if (null == sysParamInDB) {
                message = "该系统参数不存在，修改失败";
            }
            else {
                sysParamInDB.setValue1(sysParam.getValue1());
                sysParamInDB.setValue2(sysParam.getValue2());
                sysParamInDB.setValue3(sysParam.getValue3());
                addOrUpdate(sysParamInDB);
                message = "修改成功";
            }
        } catch (Exception e) {
            message = "参数异常，修改失败";
            e.printStackTrace();
        }

        return message;
    }

    public List<SysParam> search(String keywords) {
        List<SysParam> sysParams = sysParamDAO.search('%' + keywords + '%');
        for (SysParam sysParam : sysParams) {
            List<AdminRole> roles = adminRoleService.listRolesByUser(sysParam.getUserId());
            User user = new User(sysParam.getId(),sysParam.getUserUsername(),sysParam.getUserEnabled(),roles);
            sysParam.setUser(user);
        }
        return sysParams;
    }

    public List<SysParam> list() {
        List<SysParam> sysParams= sysParamDAO.findAll();
        for (SysParam sysParam : sysParams) {
            List<AdminRole> roles = adminRoleService.listRolesByUser(sysParam.getUserId());
            User user = new User(sysParam.getId(),sysParam.getUserUsername(),sysParam.getUserEnabled(),roles);
            sysParam.setUser(user);
        }
        return sysParams;
    }

    public SysParam getByUserId(int uid){
        SysParam sysParam = sysParamDAO.findByUserId(uid);
        if(sysParam==null){
            return null;
        }
        List<AdminRole> roles = adminRoleService.listRolesByUser(uid);
        User user = new User(uid,sysParam.getUserUsername(),sysParam.getUserEnabled(),roles);
        sysParam.setUser(user);
        return sysParam;
    }
}
