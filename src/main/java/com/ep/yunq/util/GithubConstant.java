package com.ep.yunq.util;

/**
 * @classname: GithubConstant
 * @Author: yan
 * @Date: 2021/4/14 17:34
 * 功能描述：
 **/
public class GithubConstant {
    // 这里填写在GitHub上注册应用时候获得 CLIENT ID
    public static final String  CLIENT_ID="f733d49acafb50cac307";
    //这里填写在GitHub上注册应用时候获得 CLIENT_SECRET
    public static final String CLIENT_SECRET="61f636a30d36290e6cb8057ba4b7096119944a93";
    // 回调路径
    public static final String CALLBACK = "http://localhost/callback";

    //获取code的url
    public static final String CODE_URL = "https://github.com/login/oauth/authorize?client_id="+CLIENT_ID+"&state=STATE&redirect_uri="+CALLBACK+"";
    //获取token的url
    public static final String TOKEN_URL = "https://github.com/login/oauth/access_token?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&code=CODE";
    //获取用户信息的url
    public static final String USER_INFO_URL = "https://api.github.com/user";
}
