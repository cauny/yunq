package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.Subject;
import com.ep.yunq.domain.service.SubjectService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @classname: SubjectController
 * @Author: yan
 * @Date: 2021/6/6 21:06
 * 功能描述：
 **/
@Api(tags = "课程管理")
@Slf4j
@RestController
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @ApiOperation("添加课程")
    @PostMapping(value = "/api/subjects")
    public Result addSubject(@RequestBody Subject subject) {
        log.info("---------------- 添加课程 ----------------------");
        String message = subjectService.add(subject);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("获取课程")
    @GetMapping(value = "/api/subjects")
    public Result<Page<Subject>> findAllSubject(@RequestParam int pageNum, @RequestParam int pageSize) {
        log.info("---------------- 获取所有课程 ----------------------");
        Page<Subject> subjects = subjectService.findAll(pageNum,pageSize);
        return ResultUtil.buildSuccessResult(subjects);

    }

    @ApiOperation("删除课程")
    @DeleteMapping(value = "/api/subjects")
    public Result deleteSubject(@RequestParam Integer subjectId) {
        log.info("---------------- 删除课程 ----------------------");
        String message = subjectService.delete(subjectId);
        if ("删除成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("修改课程")
    @PutMapping(value = "/api/subjects")
    public Result editSubject(@RequestBody Subject subject) {
        log.info("---------------- 修改课程 ----------------------");
        String message = subjectService.edit(subject);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message);
        else
            return ResultUtil.buildFailResult(message);
    }
}
