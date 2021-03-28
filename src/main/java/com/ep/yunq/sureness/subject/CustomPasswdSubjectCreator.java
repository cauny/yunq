package com.ep.yunq.sureness.subject;

import com.usthe.sureness.subject.Subject;
import com.usthe.sureness.subject.SubjectCreate;
import com.usthe.sureness.subject.support.PasswordSubject;

import javax.servlet.http.HttpServletRequest;

/**
 * @classname: CustomPasswdSubjectCreator
 * @Author: yan
 * @Date: 2021/3/28 11:36
 * 功能描述：
 **/
public class CustomPasswdSubjectCreator implements SubjectCreate {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public boolean canSupportSubject(Object context) {
        // define which request can be access
        if (context instanceof HttpServletRequest) {
            String username = ((HttpServletRequest)context).getHeader(USERNAME);
            String password = ((HttpServletRequest)context).getHeader(PASSWORD);
            return username != null && password != null;
        } else {
            return false;
        }
    }

    @Override
    public Subject createSubject(Object context) {
        // create PasswordSubject from request
        String username = ((HttpServletRequest)context).getHeader(USERNAME);
        String password = ((HttpServletRequest)context).getHeader(PASSWORD);

        String remoteHost = ((HttpServletRequest) context).getRemoteHost();
        String requestUri = ((HttpServletRequest) context).getRequestURI();
        String requestType = ((HttpServletRequest) context).getMethod();
        String targetUri = requestUri.concat("===").concat(requestType).toLowerCase();
        return PasswordSubject.builder(username, password)
                .setRemoteHost(remoteHost)
                .setTargetResource(targetUri)
                .build();
    }
}
