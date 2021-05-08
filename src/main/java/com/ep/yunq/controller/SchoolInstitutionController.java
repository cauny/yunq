package com.ep.yunq.controller;

import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.SchoolInstitution;
import com.ep.yunq.domain.service.SchoolInstitutionService;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @ApiOperation("获取所有学校机构")
    @GetMapping(value = "/api/sys/schools")
    public Result<Page<SchoolInstitutionDTO>> getAllSchoolInstitution(@RequestParam int pageNum,
                                                                      @RequestParam int pageSize) {
        log.info("---------------- 获取所有学校机构 ----------------------");
        List<SchoolInstitutionDTO> schoolInstitutions= schoolInstitutionService.getSchool(0);
        Page<SchoolInstitutionDTO> schoolInstitutionDTOS=PageUtil.listToPage(schoolInstitutions,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(schoolInstitutionDTOS);
    }

    @ApiOperation("增加学校机构")
    @PostMapping(value = "/api/sys/schools")
    public Result<String> addSchoolInstitution(@RequestBody SchoolInstitutionDTO schoolInstitutionDTO) {
        log.info("---------------- 增加学校机构 ----------------------");
        ModelMapper modelMapper = new ModelMapper();
        SchoolInstitution schoolInstitution=modelMapper.map(schoolInstitutionDTO,SchoolInstitution.class);
        String message = schoolInstitutionService.add(schoolInstitution);
        if ("添加成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("删除学校机构")
    @DeleteMapping(value = "/api/sys/schools")
    public Result<String> deleteSchoolInstitution(@RequestParam List<Integer> sids) {
        log.info("---------------- 删除学校机构 ----------------------");
        String message = schoolInstitutionService.batchDelete(sids);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除学校机构")
    @DeleteMapping(value = "/api/sys/schools/batches")
    public Result<String> deleteSchoolInstitution(@RequestParam int siid) {
        log.info("---------------- 删除学校机构 ----------------------");
        String message = schoolInstitutionService.delete(siid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改学校机构")
    @PutMapping( "/api/sys/schools")
    public Result<String> editSchoolInstitution(@RequestBody SchoolInstitutionDTO schoolInstitutionDTO) {
        log.info("---------------- 修改学校机构 ----------------------");
        ModelMapper modelMapper = new ModelMapper();
        SchoolInstitution schoolInstitution=modelMapper.map(schoolInstitutionDTO,SchoolInstitution.class);
        String message = schoolInstitutionService.edit(schoolInstitution);
        if ("修改成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message);
        }
        else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("搜索学校机构")
    @GetMapping( "/api/sys/schools/search")
    public Result<Page<SchoolInstitutionDTO>> searchSchoolInstitution(@RequestParam String keywords,
                                                                   @RequestParam int pageNum,
                                                                   @RequestParam int pageSize) {
        log.info("---------------- 搜索学校机构 ----------------------");
        List<SchoolInstitutionDTO> schoolInstitutions= schoolInstitutionService.search(keywords);
        /*Page<SchoolInstitution> schoolInstitutionsPage= PageUtil.listToPage(schoolInstitutions,pageNum,pageSize);*/
        Page<SchoolInstitutionDTO> schoolInstitutionDTOS=PageUtil.listToPage(schoolInstitutions,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(schoolInstitutionDTOS);
    }

    @ApiOperation("查找学校机构子节点")
    @GetMapping( "/api/sys/schools/children")
    public Result<List<SchoolInstitutionDTO>> findChildren(@RequestParam int sid) {
        log.info("---------------- 查找子节点 ----------------------");
        List<SchoolInstitutionDTO> schoolInstitutionDTOS=schoolInstitutionService.getSchool(sid);
        return ResultUtil.buildSuccessResult(schoolInstitutionDTOS);
    }

    /*@GetMapping("/api/userInfo/school/get")
    public Result getSchool() {
        log.info("---------------- 获取学校级别 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.getSchool(0);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }*/

    /*@ApiOperation("获取院校系")
    @GetMapping("/api/userInfo/school-info")
    public Result<List<SchoolInstitution>> getCollegeAndMajor(@RequestParam int parentId) {
        log.info("---------------- 获取院校系 ----------------------");
        List<SchoolInstitution> schoolInstitutions= schoolInstitutionService.getSchool(parentId);
        return ResultUtil.buildSuccessResult(schoolInstitutions);
    }*/
}
