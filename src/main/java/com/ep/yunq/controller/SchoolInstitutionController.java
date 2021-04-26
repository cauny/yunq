package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.SchoolInstitution;
import com.ep.yunq.domain.service.SchoolInstitutionService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @classname: SchoolInstitutionController
 * @Author: yan
 * @Date: 2021/4/26 9:12
 * 功能描述：
 **/
@Api(tags = "学校机构管理")
@Slf4j
@RestController
public class SchoolInstitutionController {

    @Autowired
    SchoolInstitutionService schoolInstitutionService;

    @GetMapping(value = "/api/sys/school/all")
    public Result getAllSchoolInstitution() {
        log.info("---------------- 获取所有学校机构 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.list();
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }

    @PostMapping(value = "/api/sys/school")
    public Result addSchoolInstitution(@RequestBody SchoolInstitution sInstitution) {
        log.info("---------------- 增加学校机构 ----------------------");
        String message = schoolInstitutionService.add(sInstitution);
        if ("添加成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @DeleteMapping(value = "/api/sys/school")
    public Result deleteSchoolInstitution(@RequestParam int siid) {
        log.info("---------------- 删除学校机构 ----------------------");
        String message = schoolInstitutionService.delete(siid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @PutMapping( "/api/sys/school")
    public Result editSchoolInstitution(@RequestBody SchoolInstitution sInstitution) {
        log.info("---------------- 修改学校机构 ----------------------");
        String message = schoolInstitutionService.edit(sInstitution);
        if ("修改成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @GetMapping( "/api/sys/school/search")
    public Result searchSchoolInstitution(@RequestParam String keywords) {
        log.info("---------------- 搜索学校机构 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.search(keywords);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }

    /*@GetMapping("/api/userInfo/school/get")
    public Result getSchool() {
        log.info("---------------- 获取学校级别 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.getSchool(0);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }*/

    @GetMapping("/api/userInfo/school-info")
    public Result getCollegeAndMajor(@RequestParam int parentId) {
        log.info("---------------- 获取院校系 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.getSchool(parentId);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }
}
