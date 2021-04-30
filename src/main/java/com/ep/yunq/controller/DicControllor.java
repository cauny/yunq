package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.DictionaryDetail;
import com.ep.yunq.domain.entity.DictionaryType;
import com.ep.yunq.domain.entity.Param;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.DictionaryDetailService;
import com.ep.yunq.domain.service.DictionaryTypeService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @classname: DicControllor
 * @Author: yan
 * @Date: 2021/4/13 0:01
 * 功能描述：
 **/
@Api(tags = "数据字典")
@Slf4j
@RestController
public class DicControllor {
    @Autowired
    DictionaryDetailService dictionaryDetailService;
    @Autowired
    DictionaryTypeService dictionaryTypeService;

    /** -------------------------- 字典类型 -------------------------------------- **/
    @ApiOperation("增加字典类型")
    @PostMapping("/api/dictionaries/dictionary-types")
    public Result<String> addDicType(@RequestParam String code, @RequestParam String name) {
        log.info("---------------- 增加字典类型 ----------------------");
        DictionaryType dictionaryType=new DictionaryType(code,name,1);

        String message = dictionaryTypeService.add(dictionaryType);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除字典类型")
    @DeleteMapping("/api/dictionaries/dictionary-types")
    public Result<String> deleteDicType(@RequestParam int dicTypeId) {
        log.info("---------------- 删除字典类型 ----------------------");
        String message = dictionaryTypeService.delete(dicTypeId);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("批量删除字典类型")
    @DeleteMapping("/api/dictionaries/dictionary-types/batch")
    public Result<String> batchDeleteDicTypes(@RequestParam List<Integer> dicTypeIds) {
        String message="";
        log.info("---------------- 批量删除字典类型 ----------------------");
        message=dictionaryTypeService.batchDelete(dicTypeIds);
        if (!message.equals("删除成功")){
            return ResultUtil.buildFailResult(message);
        }else{
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("修改字典类型")
    @PutMapping("/api/dictionaries/dictionary-types")
    public Result<String> editDicType(@RequestBody DictionaryType dictionaryType) {
        log.info("---------------- 修改字典类型 ----------------------");
        String message="";
        try{
            DictionaryType dt=dictionaryTypeService.findByCode(dictionaryType.getCode());
            if(dt!=null&&dictionaryType.getId()!=dt.getId()){
                message="字典类型英文标识重复";
                return ResultUtil.buildFailResult(message);
            }
            /*DictionaryType dictionaryType=new DictionaryType(id,code,name,status);*/
            message = dictionaryTypeService.edit(dictionaryType);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }

        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    /*@ApiOperation("修改字典类型状态")
    @PutMapping("/api/dictionary-types/status")
    public Result updateDicTypeStatus(@RequestBody DictionaryType dictionaryType) {
        log.info("---------------- 修改字典类型状态 ----------------------");
        String message = dictionaryTypeService.updateStatus(dictionaryType);
        if ("更新成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }*/

    @ApiOperation("获取所有字典类型")
    @GetMapping("/api/dictionaries/dictionary-types")
    public Result<Page<DictionaryType>> getAllDicType(@RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("---------------- 获取所有字典类型 ----------------------");
        /*List<DictionaryType> dts = dictionaryTypeService.list();*/

        Page<DictionaryType> dts=dictionaryTypeService.list(pageNum,pageSize);

        return ResultUtil.buildSuccessResult(dts);
    }


    /** -------------------------- 字典明细 -------------------------------------- **/
    @ApiOperation("增加字典明细")
    @PostMapping("/api/dictionaries/dictionary-details")
    public Result<String> addDicDetail(@RequestBody DictionaryDetail dictionaryDetail) {
        log.info("---------------- 增加字典明细 ----------------------");
        /*DictionaryType dictionaryType=dictionaryTypeService.findById(typeId);
        DictionaryDetail dictionaryDetail=new DictionaryDetail(sort,name,value,defaultValue,status,dictionaryType);*/
        String message = dictionaryDetailService.add(dictionaryDetail);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除字典明细")
    @DeleteMapping("/api/dictionaries/dictionary-details")
    public Result<String> deleteDicInfo(@RequestParam int dicDetailId) {
        log.info("---------------- 删除字典明细 ----------------------");
        String message = dictionaryDetailService.delete(dicDetailId);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("批量删除字典明细")
    @DeleteMapping("/api/dictionaries/dictionary-details/batch")
    public Result<String> batchDeleteDicDetails(@RequestParam List<Integer> dicDetailIds) {
        String message="";
        log.info("---------------- 批量删除字典明细 ----------------------");
        message=dictionaryDetailService.batchDelete(dicDetailIds);
        if (!message.equals("删除成功")){
            return ResultUtil.buildFailResult(message);
        }else{
            return ResultUtil.buildSuccessResult(message,null);
        }
    }

    @ApiOperation("修改字典字典明细")
    @PutMapping("/api/dictionaries/dictionary-details")
    public Result<String> editDicDetail(@RequestBody DictionaryDetail dictionaryDetail) {
        log.info("---------------- 修改字典明细 ----------------------");
        String message="";
        /*DictionaryType dictionaryType=dictionaryTypeService.findById(typeId);
        DictionaryDetail dictionaryDetail=new DictionaryDetail(id,sort,name,value,defaultValue,status,dictionaryType);*/
        try {
            message = dictionaryDetailService.edit(dictionaryDetail);
            if ("修改成功".equals(message))
                return ResultUtil.buildSuccessResult(message,null);
            else
                return ResultUtil.buildFailResult(message);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.buildFailResult("参数异常");
        }

    }

    /*@ApiOperation("修改字典明细状态")
    @PutMapping("/api/dictionary-details/status")
    public Result updateDicDetailStatus(@RequestBody DictionaryDetail dictionaryDetail) {
        log.info("---------------- 修改字典明细状态 ----------------------");
        String message = dictionaryDetailService.updateStatus(dictionaryDetail);
        if ("更新成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }*/

    @ApiOperation("获取所有字典明细")
    @GetMapping("/api/dictionaries/dictionary-details")
    public Result<Page<DictionaryDetail>> getAllDicDetailByDicTypeId(@RequestParam int dicTypeId,
                                             @RequestParam int pageNum,
                                             @RequestParam int pageSize) {
        log.info("---------------- 获取所有字典明细 ----------------------");
        /*List<DictionaryDetail> dis = dictionaryDetailService.findAllByTypeId(dicTypeId);*/
        Page<DictionaryDetail> dis=dictionaryDetailService.dicsList(dicTypeId,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(dis);
    }

    /*@ApiOperation("获取字典键值对")
    @GetMapping("/api/dictionary-details/{TypeCode}")
    public Result getAllByTypeCode(@PathVariable("TypeCode") int typeCode) {
        log.info("---------------- 获取字典键值对 ----------------------");
        List<Map<String, String>> kv = dictionaryDetailService.findAllByTypeCode(typeCode);
        return ResultUtil.buildSuccessResult(kv);
    }*/

    @ApiOperation("增加字典类型和字典明细")
    @PostMapping("/api/dictionaries/dictionary-types-details")
    public Result<String> addTypeAndDetail(@RequestBody Param param) {
        log.info("---------------- 增加字典类型和字典明细 ----------------------");
        String message = dictionaryDetailService.addTypeAndDetails(param.dictionaryType, param.dictionaryDetails);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

}


