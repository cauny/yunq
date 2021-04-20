package com.ep.yunq.service;

import com.ep.yunq.dao.DictionaryDetailDAO;
import com.ep.yunq.pojo.DictionaryDetail;
import com.ep.yunq.pojo.DictionaryType;
import com.ep.yunq.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @classname: DictionaryDetailService
 * @Author: yan
 * @Date: 2021/4/12 23:44
 * 功能描述：
 **/
@Slf4j
@Service
public class DictionaryDetailService {
    @Autowired
    DictionaryDetailDAO dictionaryDetailDAO;
    @Autowired
    DictionaryTypeService dictionaryTypeService;

    public void addOrUpdate(DictionaryDetail dictionaryDetail) {
        dictionaryDetail.setUpdateTime(new Date());
        dictionaryDetailDAO.save(dictionaryDetail);
    }

    public DictionaryDetail findById(int id) {
        return dictionaryDetailDAO.findById(id);
    }

    public List<DictionaryDetail> list() {
        return dictionaryDetailDAO.findAll();
    }

    public boolean isExist(DictionaryDetail info) {
        DictionaryDetail infoInDB = dictionaryDetailDAO.findByValueAndDictionaryType(info.getValue(), info.getDictionaryType().getId());
        return null!=infoInDB;
    }

    public String add(DictionaryDetail dictionaryDetail) {
        String message = "";
        try {
            if (null == dictionaryDetail.getDictionaryType()) {
                message = "字典类型为空，添加失败";
                return message;
            }
            if (isExist(dictionaryDetail)) {
                message = "字典明细已存在，添加失败";
                return message;
            }
            dictionaryDetail.setUpdateTime(new Date());
            if(dictionaryDetail.getDefaultValue()==1&&isExistDefault()){
                deleteDefaultValue();
            }

            addOrUpdate(dictionaryDetail);

            message = "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，添加失败";
        }

        return message;
    }

    public String batchAdd(List<DictionaryDetail> dictionaryDetails) {
        String message = "";
        for (DictionaryDetail info : dictionaryDetails) {
            message = add(info);
            if (!"添加成功".equals(message))
                break;
        }
        return message;
    }

    public String delete(int dicDetailId) {
        String message = "";
        try {
            dictionaryDetailDAO.deleteById(dicDetailId);

            message = "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，删除失败";
        }

        return message;
    }

    /* 批量删除 */
    public String batchDelete(List<Integer> dicDetailIds){
        String message = "";
        for(int dicDetailId:dicDetailIds){
            message = delete(dicDetailId);
            if (!message.equals("删除成功"))
                break;
        }
        return message;
    }

    public String edit(DictionaryDetail dictionaryDetail) {
        String message = "";
        try {
            DictionaryDetail dicDetailInDB = findById(dictionaryDetail.getId());

            if (null == dicDetailInDB) {
                message = "找不到该字典明细，修改失败";
                return message;
            }

            dicDetailInDB.setName(dictionaryDetail.getName());
            dicDetailInDB.setSort(dictionaryDetail.getSort());
            dicDetailInDB.setValue(dictionaryDetail.getValue());
            dicDetailInDB.setDefaultValue(dictionaryDetail.getDefaultValue());
            dicDetailInDB.setStatus(dictionaryDetail.getStatus());
            // dicDetailInDB.setDictionaryType();
            if(dictionaryDetail.getDefaultValue()==1&&isExistDefault()){
                deleteDefaultValue();
            }

            addOrUpdate(dicDetailInDB);
            message = "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，修改失败";
        }

        return message;
    }

    public String updateStatus(DictionaryDetail dictionaryDetail) {
        String message = "";
        try {
            DictionaryDetail dicDetailInDB = findById(dictionaryDetail.getId());

            if (null == dicDetailInDB) {
                message = "找不到该字典明细，更新失败";
                return message;
            }

            dicDetailInDB.setStatus(dictionaryDetail.getStatus());

            addOrUpdate(dicDetailInDB);
            message = "更新成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "参数错误，更新失败";
        }

        return message;
    }

    public List<DictionaryDetail> findAllByTypeId(int dicTypeId) {
        return dictionaryDetailDAO.findAllByDictionaryTypeOrderBySort(dicTypeId);
    }

    public List<Map<String,String>> findAllByTypeCode(int code) {
        return dictionaryDetailDAO.findAllByCode(code);
    }

    public String addTypeAndDetails(DictionaryType dictionaryType, List<DictionaryDetail> dictionaryDetails) {
        String message = "";
        try {
            dictionaryTypeService.addOrUpdate(dictionaryType);
            int id = dictionaryTypeService.getIdByCode(dictionaryType.getCode());
            dictionaryType.setId(id);
            for (DictionaryDetail dictionaryDetail: dictionaryDetails) {
                dictionaryDetail.setDictionaryType(dictionaryType);
                addOrUpdate(dictionaryDetail);
            }

            message = "添加成功";
        } catch (Exception e) {
            message = "参数异常，添加失败";
        }

        return message;
    }

    public Page<DictionaryDetail> dicsList(int typeId,int pageNumber,int pageSize){
        Sort sort=Sort.by(Sort.Direction.ASC,"sort");
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        DictionaryType dictionaryType=dictionaryTypeService.findById(typeId);
        Page<DictionaryDetail> res=dictionaryDetailDAO.findByDictionaryType(dictionaryType,pageable);
        return res;

    }

    /* 默认值是否存在 */
    public boolean isExistDefault(){
        if(dictionaryDetailDAO.findByDefaultValue(1)!=null){
            return true;
        }
        return false;
    }

    public void deleteDefaultValue(){
        DictionaryDetail dictionaryDetail=dictionaryDetailDAO.findByDefaultValue(1);
        dictionaryDetail.setDefaultValue(0);
        addOrUpdate(dictionaryDetail);
    }
}
