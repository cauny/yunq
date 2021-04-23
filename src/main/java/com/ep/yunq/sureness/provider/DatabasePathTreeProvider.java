package com.ep.yunq.sureness.provider;

import com.ep.yunq.domain.service.PermissionResourceService;
import com.usthe.sureness.matcher.PathTreeProvider;
import com.usthe.sureness.util.SurenessCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @classname: DatabasePathTreeProvider
 * @Author: yan
 * @Date: 2021/3/28 11:35
 * 功能描述：
 **/
@Slf4j
@Component
public class DatabasePathTreeProvider implements PathTreeProvider {

    @Autowired
    private PermissionResourceService permissionResourceService;

    @Override
    public Set<String> providePathData() {
        log.info(String.valueOf(permissionResourceService.getAllEnableResourcePath()));
        return SurenessCommonUtil.attachContextPath(getContextPath(), permissionResourceService.getAllEnableResourcePath());
    }

    @Override
    public Set<String> provideExcludedResource() {

        return SurenessCommonUtil.attachContextPath(getContextPath(), permissionResourceService.getAllDisableResourcePath());
    }
}
