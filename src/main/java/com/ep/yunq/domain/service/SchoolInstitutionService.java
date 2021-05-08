package com.ep.yunq.domain.service;

import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.domain.dao.SchoolInstitutionDAO;
import com.ep.yunq.domain.entity.SchoolInstitution;
import com.ep.yunq.infrastructure.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @classname: SchoolInstitutionService
 * @Author: yan
 * @Date: 2021/4/11 10:58
 * 功能描述：
 **/
@Slf4j
@Service
public class SchoolInstitutionService {
    @Autowired
    SchoolInstitutionDAO schoolInstitutionDAO;

    /* 添加或更新 */
    public void addOrUpdate(SchoolInstitution schoolInstitution){
        schoolInstitution.setUpdateTime(new Date());
        schoolInstitutionDAO.save(schoolInstitution);
    }

    public List<SchoolInstitution> getAllByParentId(int pid,Sort sort) {
        return schoolInstitutionDAO.findAllByParentId(pid,sort);
    }

    public String setLevel(int parentId){
        /* 找到父节点的等级 */
        String level =schoolInstitutionDAO.findLevelById(parentId);
        if (level==null) {
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

    @Modifying
    @Transactional
    public String delete(int siid) {
        String message = "";
        try {
            SchoolInstitution sInstitutionInDB = schoolInstitutionDAO.findById(siid);
            if (null == sInstitutionInDB) {
                message = "该机构未找到，删除失败";
            } else {
                String level = sInstitutionInDB.getLevel() + String.valueOf(siid) + ".%";
                schoolInstitutionDAO.delete(sInstitutionInDB);
                schoolInstitutionDAO.deleteAllByLevelLike(level);
                message = "删除成功";
            }

        } catch (Exception e) {
            message = "参数异常，删除失败";
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        };

        return message;
    }

    public String batchDelete(List<Integer> sids) {
        String message = "";
        for (int sid : sids) {
            message = delete(sid);
            if (!"删除成功".equals(message)) {
                break;
            }
        }
        return message;
    }

    public String edit(SchoolInstitution sInstitution) {
        String message = "";
        try {
            SchoolInstitution sInstitutionInDB= schoolInstitutionDAO.findById(sInstitution.getId());
            if (null == sInstitutionInDB) {
                message = "该机构未找到，修改失败";
            } else {
                sInstitutionInDB.setName(sInstitution.getName());
                sInstitutionInDB.setParentId(sInstitution.getParentId());
                sInstitutionInDB.setSort(sInstitution.getSort());
                String level =setLevel(sInstitution.getParentId());
                sInstitutionInDB.setLevel(level);
                addOrUpdate(sInstitutionInDB);
                message = "修改成功";
            }

        } catch (Exception e) {
            message = "参数异常，修改失败";
            e.printStackTrace();
        };

        return message;
    }
    public List<SchoolInstitutionDTO> search(String keywords){
        List<SchoolInstitution> sInstitutions = schoolInstitutionDAO.findAllByNameLike("%" + keywords + "%");
        log.info("搜索"+sInstitutions);

        List<Integer> ids=new ArrayList<>();
        for(SchoolInstitution sInstitution: sInstitutions){
            ids.add(sInstitution.getId());
        }


        return findFa(ids);
    }

    /*public List<SchoolInstitution> search(String keywords) {
        List<SchoolInstitution> sInstitutions = schoolInstitutionDAO.findAllByNameLike("%" + keywords + "%");
        log.info("搜索"+sInstitutions);
//        List<SchoolInstitution> list = list();
        List<SchoolInstitutionDTO> listDTO = getSchool(0);
        List<SchoolInstitution> list= PageUtil.listChange(listDTO,SchoolInstitution.class);
        Iterator<SchoolInstitution> iterator = list.iterator();
        List<String> idList =new ArrayList<>();
        for (SchoolInstitution sInstitution: sInstitutions) {
            String[] tag = sInstitution.getLevel().split("\\.");
            log.info("tag"+tag);
            idList.addAll(Arrays.asList(tag));
            idList.add(String.valueOf(sInstitution.getId()));
        }
        LinkedHashSet<String> hashSet =new LinkedHashSet<>(idList);
        log.info(" "+idList);
        while (iterator.hasNext()) {
            SchoolInstitution sInstitution = iterator.next();
            log.info("    "+sInstitution.getName());
            if (!hashSet.contains(String.valueOf(sInstitution.getId()))) {
                iterator.remove();
            }
        }
        return list;

   *//*     for (SchoolInstitution sInstitution: sInstitutions) {
            List<SchoolInstitution> colleges=getAllByParentId(sInstitution.getId());
            sInstitution.setChildren(colleges);
            for(SchoolInstitution college:colleges){
                college.setChildren(getAllByParentId(college.getId()));
            }
        }
        return sInstitutions;*//*
    }
*/

    /*public List<SchoolInstitution> list() {
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        List<SchoolInstitution> sInstitutions = schoolInstitutionDAO.findAll(sort);

        for (SchoolInstitution sInstitution: sInstitutions) {
            List<SchoolInstitution> colleges=getAllByParentId(sInstitution.getId());
            sInstitution.setChildren(colleges);
            for(SchoolInstitution college:colleges){
                college.setChildren(getAllByParentId(college.getId()));
            }
        }

        Iterator<SchoolInstitution> iterator = sInstitutions.iterator();
        while (iterator.hasNext()) {
            SchoolInstitution sInstitution = iterator.next();
            if (sInstitution.getParentId() != 0) {
                iterator.remove();
            }
        }

        return sInstitutions;
    }*/
    public List<SchoolInstitutionDTO> getSchool(int pid) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        List<SchoolInstitution> schoolInstitutions=getAllByParentId(pid,sort);
        List<SchoolInstitutionDTO> schoolInstitutionDTOS= PageUtil.listChange(schoolInstitutions,SchoolInstitutionDTO.class);
        for(SchoolInstitutionDTO si:schoolInstitutionDTOS){
            si.setChildren(getSchool(si.getId()));
            if(getSchool(si.getId()).size()==0){
                si.setHasChildren(false);
            }else{
                si.setHasChildren(true);
            }
        }
        return schoolInstitutionDTOS;
    }

    public List<SchoolInstitutionDTO> searchSchool(int pid,String tag[],int count){
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        List<SchoolInstitution> schoolInstitutions=getAllByParentId(pid,sort);
        List<SchoolInstitutionDTO> t= PageUtil.listChange(schoolInstitutions,SchoolInstitutionDTO.class);
        List<SchoolInstitutionDTO> schoolInstitutionDTOS=new ArrayList<>();

        if(count==0){
            for(SchoolInstitutionDTO s:t){
                if(s.getId()==Integer.parseInt(tag[count])){
                    schoolInstitutionDTOS.add(s);
                }
            }
            count++;
        }else{
            schoolInstitutionDTOS=t;
        }
        if(tag.length<count){
            List<SchoolInstitutionDTO> a=new ArrayList<>();
            return a;
        }
        for(SchoolInstitutionDTO s:schoolInstitutionDTOS){
            log.info("s.id:"+s.getId());

            List<SchoolInstitutionDTO> tmp=searchSchool(s.getId(),tag,count+1);
            List<SchoolInstitutionDTO> newList =new ArrayList<>();
            for(SchoolInstitutionDTO schoolInstitutionDTO:tmp){
                log.info("pid:"+schoolInstitutionDTO.getId()+"     tag:"+tag[count]);
                if(schoolInstitutionDTO.getId()==Integer.parseInt(tag[count])){
                    log.info("111");
                    newList.add(schoolInstitutionDTO);
                }
            }
            s.setChildren(newList);
            if(newList.size()==0){
                s.setHasChildren(false);
            }else{
                s.setHasChildren(true);
            }
        }
        return schoolInstitutionDTOS;
    }

    public SchoolInstitutionDTO findFather(SchoolInstitutionDTO s){
        SchoolInstitution schoolInstitution=schoolInstitutionDAO.findById(s.getParentId());
        if(s.getParentId()==0){
            return s;
        }
        ModelMapper modelMapper=new ModelMapper();
        SchoolInstitutionDTO schoolInstitutionDTO=modelMapper.map(schoolInstitution,SchoolInstitutionDTO.class);

        List<SchoolInstitutionDTO> tmp=new ArrayList<>();
        tmp.add(s);
        schoolInstitutionDTO.setChildren(tmp);
        schoolInstitutionDTO.setHasChildren(true);
        return findFather(schoolInstitutionDTO);
    }
    public List<SchoolInstitutionDTO> findFa(List<Integer> ids){
        List<SchoolInstitutionDTO> res =new ArrayList<>();
        for(int id:ids){
            ModelMapper modelMapper=new ModelMapper();
            SchoolInstitution schoolInstitution=schoolInstitutionDAO.findById(id);
            SchoolInstitutionDTO schoolInstitutionDTO=modelMapper.map(schoolInstitution,SchoolInstitutionDTO.class);
            res.add(findFather(schoolInstitutionDTO));
        }
        return res;
    }


}
