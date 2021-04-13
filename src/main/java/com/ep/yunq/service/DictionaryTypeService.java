package com.ep.yunq.service;

import com.ep.yunq.dao.DictionaryTypeDAO;
import com.ep.yunq.pojo.DictionaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @classname: DictionaryTypeService
 * @Author: yan
 * @Date: 2021/4/12 23:43
 * 功能描述：
 **/
@Service
public class DictionaryTypeService {
    @Autowired
    DictionaryTypeDAO dictionaryTypeDAO;

    DictionaryType findById(int id){
        return dictionaryTypeDAO.findById(id);
    }

    public void addOrUpdate(DictionaryType dictionaryType) {
        dictionaryType.setUpdateTime(new Date());
        dictionaryTypeDAO.save(dictionaryType);
    }

    public List<DictionaryType> list() {
        Sort sort = Sort.by(Sort.Direction.ASC, "code");
        return dictionaryTypeDAO.findAll(sort);
    }

    public  String add(DictionaryType dictionaryType) {
        String message = "";
        try {
            addOrUpdate(dictionaryType);

            message = "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，添加失败";
        }

        return message;
    }

    public  String delete(int dicTypeId) {
        String message = "";
        try {
            dictionaryTypeDAO.deleteById(dicTypeId);

            message = "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }

        return message;
    }

    public String edit(DictionaryType dictionaryType){
        String message = "";
        try {
            DictionaryType dicTypeInDB = findById(dictionaryType.getId());
            if (null == dicTypeInDB) {
                message = "找不到该字典类型，修改失败";
                return message;
            }

            dicTypeInDB.setCode(dictionaryType.getCode());
            dicTypeInDB.setName(dictionaryType.getName());
            addOrUpdate(dicTypeInDB);

            message = "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，修改失败";
        }

        return message;
    }

    public String updateStatus(DictionaryType dictionaryType){
        String message = "";
        try {
            DictionaryType dicTypeInDB = findById(dictionaryType.getId());

            if (null == dicTypeInDB) {
                message = "找不到该字典类型，更新失败";
                return message;
            }
            dicTypeInDB.setStatus(dictionaryType.isStatus());
            addOrUpdate(dicTypeInDB);

            message = "更新成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，更新失败";
        }

        return message;
    }

    public int getIdByCode(int code) {
        DictionaryType dictionaryType = dictionaryTypeDAO.findByCode(code);
        if (null != dictionaryType)
            return dictionaryType.getId();
        return -1;
    }
}
