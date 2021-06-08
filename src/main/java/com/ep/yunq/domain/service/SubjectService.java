package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.SysParamDTO;
import com.ep.yunq.domain.dao.SubjectDAO;
import com.ep.yunq.domain.entity.Subject;
import com.ep.yunq.domain.entity.SysParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @classname: SubjectService
 * @Author: yan
 * @Date: 2021/6/6 20:59
 * 功能描述：
 **/
@Service
public class SubjectService {
    @Autowired
    SubjectDAO subjectDAO;

    public Subject findById(int id){return subjectDAO.findById(id);}
    public Subject findByName(String name){return subjectDAO.findByName(name);}

    public Page<Subject> findAll(int pageNumber,int pageSize){
        Sort sort=Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Subject> res=subjectDAO.findAll(pageable);
        return res;
    }
    public void addOrUpdate(Subject subject) {
        subjectDAO.save(subject);
    }

    public String add(Subject subject) {
        String message = "";
        try {
            addOrUpdate(subject);
            message = "添加成功";
        } catch (Exception e) {
            message = "参数异常，添加失败";
            e.printStackTrace();
        }
        return message;
    }
    public String edit(Subject subject) {
        String message = "";
        try {
            Subject subject1 = subjectDAO.findById(subject.getId());
            if (null == subject1) {
                message = "该课程不存在，修改失败";
            }
            else {
                addOrUpdate(subject);
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
            subjectDAO.deleteById(id);
            message = "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }
        return message;
    }

}
