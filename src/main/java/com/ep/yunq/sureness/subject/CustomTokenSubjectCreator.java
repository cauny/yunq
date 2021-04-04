package com.ep.yunq.sureness.subject;

import com.usthe.sureness.subject.Subject;
import com.usthe.sureness.subject.SubjectCreate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @classname: CustomTokenSubjectCreator
 * @Author: yan
 * @Date: 2021/3/28 11:37
 * 功能描述：
 **/
@Slf4j
public class CustomTokenSubjectCreator implements SubjectCreate {

    private static final Logger logger = LoggerFactory.getLogger(CustomTokenSubjectCreator.class);

    private static final String HEADER_TOKEN = "Token";
    private static final String TOKEN_SPLIT = "--";
    private static final int TOKEN_SPLIT_SIZE = 4;

    @Override
    public boolean canSupportSubject(Object context) {
        // support token
        // {"Token" : "tokenValue"}
        if (context instanceof HttpServletRequest) {
            String authorization = ((HttpServletRequest)context).getHeader(HEADER_TOKEN);
            return authorization != null && authorization.split(TOKEN_SPLIT).length == TOKEN_SPLIT_SIZE;
        }
        return false;
    }

    @Override
    public Subject createSubject(Object context) {
        log.info("11111111111");
        String authorization = ((HttpServletRequest)context).getHeader(HEADER_TOKEN);
        String remoteHost = ((HttpServletRequest) context).getRemoteHost();
        String requestUri = ((HttpServletRequest) context).getRequestURI();
        String requestType = ((HttpServletRequest) context).getMethod();
        String targetUri = requestUri.concat("===").concat(requestType.toLowerCase());
        return CustomTokenSubject.builder(authorization)
                .setRemoteHost(remoteHost)
                .setTargetResource(targetUri)
                .build();
    }
}
