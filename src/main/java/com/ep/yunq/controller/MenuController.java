package com.ep.yunq.controller;

import com.ep.yunq.application.dto.MenuDTO;
import com.ep.yunq.application.dto.SchoolInstitutionDTO;
import com.ep.yunq.domain.entity.AdminMenu;
import com.ep.yunq.domain.entity.Result;
import com.ep.yunq.domain.entity.SchoolInstitution;
import com.ep.yunq.domain.service.AdminMenuService;
import com.ep.yunq.infrastructure.util.CommonUtil;
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

    @ApiOperation("获取当前用户菜单")
    @GetMapping("/api/menus/ids")
    public Result<Page<MenuDTO>> getMenu(@RequestParam int pageNum,
                                           @RequestParam int pageSize){
        log.info("---------------- 获取用户菜单 ----------------------");
        Integer uid= CommonUtil.getTokenId();
        if(uid==null){
            return ResultUtil.buildFailResult("Token出错");
        }
        List<MenuDTO> menus = adminMenuService.getMenusByUserId(uid);
        Page<MenuDTO> menuPage=PageUtil.listToPage(menus,pageNum,pageSize);
        if (0 != menuPage.getContent().size()) {
            return ResultUtil.buildSuccessResult(menuPage);
        }
        else {
            return ResultUtil.buildFailResult("当前用户无菜单");
        }

    }

    @ApiOperation("获取角色菜单")
    @GetMapping("/api/menus/roles")
    public Result<Page<MenuDTO>> listAllMenusByRoleId(@RequestParam Integer roleId,
                                                        @RequestParam int pageNum,
                                                        @RequestParam int pageSize) {
        log.info("---------------- 获取角色菜单 ----------------------");
        List<MenuDTO> menus = adminMenuService.getMenusByRoleId(roleId);
        Page<MenuDTO> menuPage=PageUtil.listToPage(menus,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(menuPage);
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
    public Result<String> batchDeleteMenu(@RequestBody List<Integer> menuIds) {
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
    public Result<Page<MenuDTO>> search(@RequestParam String keywords,
                                          @RequestParam int pageNum,
                                          @RequestParam int pageSize){
        log.info("---------------- 搜索菜单 ----------------------");
        List<MenuDTO> ms = adminMenuService.search(keywords);
        Page<MenuDTO> menuPage=PageUtil.listToPage(ms,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(menuPage);
    }

    @ApiOperation("获取所有菜单")
    @GetMapping(value = "/api/menus")
    public Result<Page<MenuDTO>> all(@RequestParam int pageNum,
                                       @RequestParam int pageSize){
        log.info("---------------- 获取所有菜单 ----------------------");
        List<MenuDTO> ms = adminMenuService.all();
        Page<MenuDTO> menuPage=PageUtil.listToPage(ms,pageNum,pageSize);
        return ResultUtil.buildSuccessResult(menuPage);
    }

    @ApiOperation("查找学校机构子节点")
    @GetMapping( "/api/menus/children")
    public Result<List<MenuDTO>> findChildren(@RequestParam int mid) {
        log.info("---------------- 查找子节点 ----------------------");
        List<MenuDTO> adminMenus= adminMenuService.findAllByParentId(mid);
        return ResultUtil.buildSuccessResult(adminMenus);
    }

    @ApiOperation("查找学校机构父节点")
    @GetMapping( "/api/menus/father")
    public Result<String> findFatherName(@RequestParam int mid) {
        log.info("---------------- 查找子节点 ----------------------");
        String result= adminMenuService.findFather(mid);
        return ResultUtil.buildSuccessResult(result);
    }

}
