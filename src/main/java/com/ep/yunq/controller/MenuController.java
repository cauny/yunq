package com.ep.yunq.controller;

import com.ep.yunq.domain.entity.AdminMenu;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.service.AdminMenuService;
import com.ep.yunq.infrastructure.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/api/menu")
    public Result getMenu(@RequestParam Integer userId){
        log.info("---------------- 获取用户菜单 ----------------------");

        List<AdminMenu> menus = adminMenuService.getMenusByUserId(userId);
        if (0 != menus.size()) {
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
    @GetMapping("/api/menu/role")
    public Result listAllMenusByRoleId(@RequestParam Integer roleId) {
        log.info("---------------- 获取角色菜单 ----------------------");
        List<AdminMenu> menus = adminMenuService.getMenusByRoleId(roleId);
        return ResultUtil.buildSuccessResult(menus);
    }

    @ApiOperation("添加菜单")
    @PostMapping("api/menu/add")
    public Result addMenu(@RequestBody AdminMenu adminMenu){
        log.info("---------------- 添加菜单 ----------------------");
        String message = adminMenuService.add(adminMenu);
        if ("添加成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("删除菜单")
    @GetMapping("/api/menu/delete")
    public Result deleteMenu(@RequestParam int mid) {
        log.info("---------------- 删除菜单 ----------------------");
        String message = adminMenuService.delete(mid);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("批量删除菜单")
    @PostMapping("/api/menu/delete")
    public Result batchDeleteMenu(@RequestBody LinkedHashMap menuIds) {
        log.info("---------------- 批量删除菜单 ----------------------");
        String message = adminMenuService.batchDelete(menuIds);
        if ("删除成功".equals(message)) {
            return ResultUtil.buildSuccessResult(message,null);
        } else {
            return ResultUtil.buildFailResult(message);
        }
    }

    @ApiOperation("修改菜单")
    @PutMapping("/api/menu/edit")
    public Result editMenu(@RequestBody AdminMenu adminMenu) {
        log.info("---------------- 修改菜单 ----------------------");
        String message = adminMenuService.edit(adminMenu);
        if ("修改成功".equals(message))
            return ResultUtil.buildSuccessResult(message,null);
        else
            return ResultUtil.buildFailResult(message);
    }

    @ApiOperation("搜索菜单")
    @GetMapping(value = "/api/menu/search")
    public Result search(@RequestParam String keywords){
        log.info("---------------- 搜索菜单 ----------------------");
        List<AdminMenu> ms = adminMenuService.search(keywords);
        return ResultUtil.buildSuccessResult(ms);
    }

    @ApiOperation("获取所有菜单")
    @GetMapping(value = "/api/menu/all")
    public Result all(){
        log.info("---------------- 获取所有菜单 ----------------------");
        List<AdminMenu> ms = adminMenuService.all();
        return ResultUtil.buildSuccessResult(ms);
    }


}
