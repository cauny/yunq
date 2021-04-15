package com.ep.yunq.controller;

import com.ep.yunq.pojo.DictionaryDetail;
import com.ep.yunq.pojo.DictionaryType;
import com.ep.yunq.pojo.Param;
import com.ep.yunq.pojo.Result;
import com.ep.yunq.service.DictionaryDetailService;
import com.ep.yunq.service.DictionaryTypeService;
import com.ep.yunq.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @PostMapping("/api/dictionary-types")
    public Result addDicType(@RequestBody DictionaryType dictionaryType) {
        log.info("---------------- 增加字典类型 ----------------------");
        String message = dictionaryTypeService.add(dictionaryType);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除字典类型")
    @DeleteMapping("/api/dictionary-types")
    public Result deleteDicType(@RequestParam int dicTypeId) {
        log.info("---------------- 删除字典类型 ----------------------");
        String message = dictionaryTypeService.delete(dicTypeId);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改字典类型")
    @PutMapping("/api/dictionary-types")
    public Result editDicType(@RequestBody DictionaryType dictionaryType) {
        log.info("---------------- 修改字典类型 ----------------------");
        String message = dictionaryTypeService.edit(dictionaryType);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    /*@ApiOperation("修改字典类型状态")
    @PutMapping("/api/dictionary-types/status")
    public Result updateDicTypeStatus(@RequestBody DictionaryType dictionaryType) {
        log.info("---------------- 修改字典类型状态 ----------------------");
        String message = dictionaryTypeService.updateStatus(dictionaryType);
        if ("更新成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }*/

    @ApiOperation("获取所有字典类型")
    @GetMapping("/api/dictionary-types")
    public Result getAllDicType() {
        log.info("---------------- 获取所有字典类型 ----------------------");
        List<DictionaryType> dts = dictionaryTypeService.list();

        return ResultUtil.buildSuccessResult(dts);
    }


    /** -------------------------- 字典明细 -------------------------------------- **/
    @ApiOperation("增加字典明细")
    @PostMapping("/api/dictionary-details")
    public Result addDicDetail(@RequestBody DictionaryDetail dictionaryDetail) {
        log.info("---------------- 增加字典明细 ----------------------");
        String message = dictionaryDetailService.add(dictionaryDetail);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除字典明细")
    @DeleteMapping("/api/dictionary-details")
    public Result deleteDicInfo(@RequestParam int dicDetailId) {
        log.info("---------------- 删除字典字典明细 ----------------------");
        String message = dictionaryDetailService.delete(dicDetailId);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改字典字典明细")
    @PutMapping("/api/dictionary-details")
    public Result editDicDetail(@RequestBody DictionaryDetail dictionaryDetail) {
        log.info("---------------- 修改字典明细 ----------------------");
        String message = dictionaryDetailService.edit(dictionaryDetail);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    /*@ApiOperation("修改字典明细状态")
    @PutMapping("/api/dictionary-details/status")
    public Result updateDicDetailStatus(@RequestBody DictionaryDetail dictionaryDetail) {
        log.info("---------------- 修改字典明细状态 ----------------------");
        String message = dictionaryDetailService.updateStatus(dictionaryDetail);
        if ("更新成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }*/

    @ApiOperation("获取所有字典明细")
    @GetMapping("/api/dictionary-details")
    public Result getAllDicDetailByDicTypeId(@RequestParam int dicTypeId) {
        log.info("---------------- 获取所有字典明细 ----------------------");
        List<DictionaryDetail> dis = dictionaryDetailService.findAllByTypeId(dicTypeId);

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
    @PostMapping("/api/dictionary-types-details")
    public Result addTypeAndDetail(@RequestBody Param param) {
        log.info("---------------- 增加字典类型和字典明细 ----------------------");
        String message = dictionaryDetailService.addTypeAndDetails(param.dictionaryType, param.dictionaryDetails);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

}


