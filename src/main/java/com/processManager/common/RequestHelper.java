package com.processManager.common;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper implements IRequestHelper {
    @Autowired
    HttpServletRequest request;

    public String getTokenFromRequest() {
        return request.getHeader("Authorization");
    }
}
