package com.ep.yunq.controller;

import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.domain.entity.AdminMenu;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.SchoolInstitution;
import com.ep.yunq.domain.service.AdminMenuService;
import com.ep.yunq.infrastructure.util.PageUtil;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: MenuController
 * @Author: yan
 * @Date: 2021/4/21 21:17
 * 功能描述：
 **/
@Api(tags = "菜单管理")
@Slf4j
@RestController
public class MenuController {
    @Autowired
    AdminMenuService adminMenuService;

    @ApiOperation("获取用户菜单")
    @GetMapping("/api/menus/{userId}")
    public Result<Page<AdminMenu>> getMenu(@PathVariable("userId") Integer userId,
                                           @RequestParam int pageNum,
                                           @RequestParam int pageSize){
        log.info("---------------- 获取用户菜单 ----------------------");

        Page<AdminMenu> menus = adminMenuService.getMenusByUserId(userId,pageNum,pageSize);
        if (0 != menus.getContent().size()) {
            return ResultUtil.buildSuccessResult(menus);
        }
        else {
            /*AdminMenu menu = adminMenuService.findById(1);
            menus.add(menu);
            menu = adminMenuService.findById(5);
            menus.add(menu);*/
            return ResultUtil.buildFailResult("当前用户无菜单");
        }

    }

    @ApiOperation("获取角色菜单")
    @GetMapping("/api/menus/roles")
    public Result<Page<AdminMenu>> listAllMenusByRoleId(@RequestParam Integer roleId,
                                                        @RequestParam int pageNum,
                                                        @RequestParam int pageSize) {
        log.info("---------------- 获取角色菜单 ----------------------");
        Page<AdminMenu> menus = adminMenuService.getMenusByRoleId(roleId,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(menus);
    }

    @ApiOperation("添加菜单")
    @PostMapping("api/menus")
    public Result<String> addMenu(@RequestBody AdminMenu adminMenu){
        log.info("---------------- 添加菜单 ----------------------");
        String message = adminMenuService.add(adminMenu);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/api/menus")
    public Result<String> deleteMenu(@RequestParam int mid) {
        log.info("---------------- 删除菜单 ----------------------");
        String message = adminMenuService.delete(mid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除菜单")
    @DeleteMapping("/api/menus/batches")
    public Result<String> batchDeleteMenu(@RequestBody LinkedHashMap menuIds) {
        log.info("---------------- 批量删除菜单 ----------------------");
        String message = adminMenuService.batchDelete(menuIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改菜单")
    @PutMapping("/api/menus")
    public Result<String> editMenu(@RequestBody AdminMenu adminMenu) {
        log.info("---------------- 修改菜单 ----------------------");
        String message = adminMenuService.edit(adminMenu);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("搜索菜单")
    @GetMapping(value = "/api/menus/search")
    public Result<Page<AdminMenu>> search(@RequestParam String keywords,
                                          @RequestParam int pageNum,
                                          @RequestParam int pageSize){
        log.info("---------------- 搜索菜单 ----------------------");
        Page<AdminMenu> ms = adminMenuService.search(keywords,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(ms);
    }

    @ApiOperation("获取所有菜单")
    @GetMapping(value = "/api/menus")
    public Result<Page<AdminMenu>> all(@RequestParam int pageNum,
                                       @RequestParam int pageSize){
        log.info("---------------- 获取所有菜单 ----------------------");
        Page<AdminMenu> ms = adminMenuService.all(pageNum,pageSize);
        return ResultUtil.buildSuccessResult(ms);
    }

    @ApiOperation("查找学校机构子节点")
    @GetMapping( "/api/menus/children")
    public Result<List<AdminMenu>> findChildren(@RequestParam int mid) {
        log.info("---------------- 查找子节点 ----------------------");
        List<AdminMenu> adminMenus= adminMenuService.findAllByParentId(mid);
        /*List<SchoolInstitutionDTO> schoolInstitutionDTOS= PageUtil.listChange(schoolInstitutions,SchoolInstitutionDTO.class);*/
        return ResultUtil.buildSuccessResult(adminMenus);
    }

}
