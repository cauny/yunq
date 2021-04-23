package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.SchoolInstitutionDAO;
import com.ep.yunq.domain.entity.SchoolInstitution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @classname: SchoolInstitutionService
 * @Author: yan
 * @Date: 2021/4/11 10:58
 * 功能描述：
 **/
@Service
public class SchoolInstitutionService {
    @Autowired
    SchoolInstitutionDAO schoolInstitutionDAO;

    /* 添加或更新 */
    public void addOrUpdate(SchoolInstitution schoolInstitution){
        schoolInstitution.setUpdateTime(new Date());
        schoolInstitutionDAO.save(schoolInstitution);
    }

    public List<SchoolInstitution> getAllByParentId(int pid) {
        return schoolInstitutionDAO.findAllByParentId(pid);
    }

    public String setLevel(int parentId){
        /* 找到父节点的等级 */
        String level =schoolInstitutionDAO.findLevelById(parentId);
        if (level.isEmpty()) {
            level = String.valueOf(parentId) + ".";
        } else {
            level = level  + String.valueOf(parentId) + ".";
        }
        return level;

    }

    public String add(SchoolInstitution schoolInstitution){
        String message="";
        try {
            String level=setLevel(schoolInstitution.getParentId());
            schoolInstitution.setLevel(level);
            addOrUpdate(schoolInstitution);
            message="添加成功";
        }catch (Exception e){
            message="参数异常，添加失败";
            e.printStackTrace();
        }
        return message;
    }

    /*@Modifying
    @Transactional
    public String delete(int id){
        String message="";
        try {
            SchoolInstitution schoolInstitution= schoolInstitutionDAO.findById(id);
            if(schoolInstitution==null){
                message="该机构未找到，删除失败";
            }else{
                String level=s
            }
        }
    }*/


}
