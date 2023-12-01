package com.pm.crm.interceptors;

import com.pm.crm.exceptions.NoLoginException;
import com.pm.crm.service.UserService;
import com.pm.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (null == userId || userService.selectByPrimaryKey(userId) == null) {
            throw new NoLoginException();
        }
        return true;
    }
}
