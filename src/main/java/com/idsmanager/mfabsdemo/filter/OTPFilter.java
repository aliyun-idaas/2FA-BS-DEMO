/*
 * Copyright (c) 2016 BeiJing JZYT Technology Co. Ltd
 * www.idsmanager.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BeiJing JZYT Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with BeiJing JZYT Technology Co. Ltd.
 */
package com.idsmanager.mfabsdemo.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 1.0.0
 */
@Order(1)
@WebFilter(filterName = "otpFilter", urlPatterns = "/*")
public class OTPFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * 当正在进行多因子 认证时，若访问其他URL，将强制跳转回多因子认证；
     * 若未认证则跳转到 登录
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String contextPath = httpServletRequest.getContextPath();
        Object otpO = httpServletRequest.getSession().getAttribute("otp");
        int otp = otpO == null ? 0 : (int) otpO;
        if (otp != 2) {
            String servletPath = httpServletRequest.getServletPath();
            if (otp == 0) {
                if (!"/".equals(servletPath) && !"/login".equals(servletPath)) {
                    httpServletResponse.sendRedirect(contextPath + "/");
                    return;
                }
            } else {
                if (!"/otp".equals(servletPath) && !"/public/otp/callback".equals(servletPath)) {
                    httpServletResponse.sendRedirect(contextPath + "/otp");
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
