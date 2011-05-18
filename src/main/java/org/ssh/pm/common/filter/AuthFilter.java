package org.ssh.pm.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

/**
 * checkSessionKey 需检查的在 Session 中保存的关键字 redirectURL 如果用户未登录， 则重定向到指定的页面，URL不包括 ContextPath。
 * notCheckURLList 不做检查的URL列表，以分号分开，并且 URL 中不包括 ContextPath。 用户如果在地址栏直接输入地址，主界面将回到登录后的初始状态。
 */
public class AuthFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    protected FilterConfig filterConfig = null;

    private String redirectURL = null;

    private String mainURL = null;

    private List<String> notCheckURLList = new ArrayList<String>();

    private String sessionKey = null;

    private String uri = null;

    AntPathMatcher p = new AntPathMatcher();

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        if (sessionKey == null) {
            filterChain.doFilter(request, response);
            return;
        }
        uri = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        String HTTP_REFERER = request.getHeader("Referer");
        if (session.getAttribute(sessionKey) == null) {
            if (!checkRequestURIIntNotFilterList(request)) {
                response.sendRedirect(request.getContextPath() + redirectURL);
                return;
            }
        } else {
            if (HTTP_REFERER == null && uri.indexOf(mainURL) == -1 && uri.indexOf("login.htm") == -1) {
                response.getWriter().write("<script language=javascript>window.history.go(-1);</script>");
                response.flushBuffer();
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        notCheckURLList.clear();
    }

    //考虑 /common/*
    private boolean checkRequestURIIntNotFilterList(HttpServletRequest request) {
//        boolean f = false;
//        for (String pattern : notCheckURLList) {
//            f = p.match(pattern, uri);
//            if (f) break;
//            //if (uri.endsWith(one))
//            //	return true;
//
//        }
//        return f;
        if ((uri.startsWith("/common/")) || uri.startsWith("/resources/")) return true;
        //return (notCheckURLList.contains(uri));
        return false;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        redirectURL = filterConfig.getInitParameter("redirectURL");
        mainURL = filterConfig.getInitParameter("mainURL");
        sessionKey = filterConfig.getInitParameter("checkSessionKey");
        String notCheckURLListStr = filterConfig.getInitParameter("notCheckURLList");
        if (notCheckURLListStr != null) {
            StringTokenizer st = new StringTokenizer(notCheckURLListStr, ",");
            notCheckURLList.clear();
            while (st.hasMoreTokens()) {
                notCheckURLList.add(st.nextToken());
            }
        }
    }

    public static void main(String[] args) {
        String s = "/common/login";
        if (s.startsWith("/common/")) {
            System.out.println("ok");
        }else{
            System.out.println("oksss");
        }
    }
}