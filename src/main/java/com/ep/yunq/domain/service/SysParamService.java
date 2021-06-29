package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.SysParamDAO;
import com.ep.yunq.application.dto.SysParamDTO;
import com.ep.yunq.domain.entity.AdminRole;
import com.ep.yunq.domain.entity.SysParam;
import com.ep.yunq.domain.entity.User;
import com.ep.yunq.infrastructure.util.CommonUtil;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    @Autowired
    UserService userService;

    public SysParam findById(int id){
        return sysParamDAO.findById(id);
    }

    public void addOrUpdate(SysParam sysParam) {
        sysParamDAO.save(sysParam);
    }

    public String createUserSysParam(User user){
        String message = "";
        try{
            addOrUpdate(new SysParam("signInExperience","签到经验","2",new Date(),user));
            addOrUpdate(new SysParam("signInRange","签到距离","500",new Date(),user));
            addOrUpdate(new SysParam("classTime","每节课时长","45",new Date(),user));
            addOrUpdate(new SysParam("level_1","等级1","60",new Date(),user));
            addOrUpdate(new SysParam("level_2","等级2","80",new Date(),user));
            addOrUpdate(new SysParam("level_3","等级3","90",new Date(),user));
            addOrUpdate(new SysParam("defaultPwd","默认密码","123456",new Date(),user));
            message = "添加成功";
        }catch (Exception e) {
            message = "参数异常，添加失败";
            e.printStackTrace();
        }
        return message;

    }

    public String add(SysParamDTO sysParamDTO) {
        String message = "";
        try {
            ModelMapper modelMapper=new ModelMapper();
            SysParam sysParam=modelMapper.map(sysParamDTO,SysParam.class);
            sysParam.setUpdateTime(new Date());
            addOrUpdate(sysParam);
            message = "添加成功";
        } catch (Exception e) {
            message = "参数异常，添加失败";
            e.printStackTrace();
        }

        return message;
    }

    public String edit(SysParamDTO sysParamDTO) {
        String message = "";
        Integer uid= CommonUtil.getTokenId();
        if(uid==null){
            message="Token出错";
            return message;
        }
        try {
            SysParam sysParamInDB = sysParamDAO.findById(sysParamDTO.getId());
            if (null == sysParamInDB) {
                message = "该系统参数不存在，修改失败";
            }
            else {
                ModelMapper modelMapper=new ModelMapper();
                SysParam sysParam=modelMapper.map(sysParamDTO,SysParam.class);
                User user=userService.findById(uid);
                sysParam.setUser(user);
                addOrUpdate(sysParam);
                message = "修改成功";
            }
        } catch (Exception e) {
            message = "参数异常，修改失败";
            e.printStackTrace();
        }

        return message;
    }

    public String delete(Integer id) {
        String message = "";
        try {

            SysParam sysParamInDB = findById(id);
            if (null == sysParamInDB) {
                message = "该系统参数不存在，删除失败";
            }
            else {
                sysParamDAO.deleteById(id);
                message = "删除成功";
            }
        } catch (Exception e) {
            message = "参数异常，删除失败";
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

    public Page<SysParam> list(int pageNumber,int pageSize) {
        Sort sort=Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<SysParam> res=sysParamDAO.findAll(pageable);
        return res;
    }

    public List<SysParam> getByUserId(int uid){
        List<SysParam> sysParam = sysParamDAO.findByUserId(uid);
        if(sysParam==null){
            return null;
        }
        /*List<AdminRole> roles = adminRoleService.listRolesByUser(uid);
        User user = new User(uid,sysParam.getUserUsername(),sysParam.getUserEnabled(),roles);
        sysParam.setUser(user);*/
        return sysParam;
    }
    public SysParam findByUserIdAndName(int uid,String name){
        return sysParamDAO.findByUserIdAndName(uid,name);

    }
}
