package com.ep.yunq.infrastructure.util;

import java.util.UUID;

/**
 * @classname: CommonUtil
 * @Author: yan
 * @Date: 2021/4/12 11:39
 * 功能描述：
 **/
public class CommonUtil {

    public static String creatUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
