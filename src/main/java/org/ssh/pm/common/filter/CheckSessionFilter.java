package org.ssh.pm.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 用户请求过滤器，用以判断当前用户的Session是否超时，若超时则跳转至登录页面
 */
public class CheckSessionFilter implements Filter {
	public static final String EXCLUDE_URI = "excludeUri";

	public static final String INCLUDE_SUFFIXS_NAME = "includeSuffixs";

	public static final String LOGIN_URI_NAME = "loginUri";

	private static final String[] DEFAULT_EXCLUDE_URI = { "login.htm",
			"logon.htm", "main.htm", "logout.htm", "initData.htm",
			"updatepassword.htm" };

	private static final String[] DEFAULT_INCLUDE_SUFFIXS = { ".htm" };

	private static final String DEFAULT_LOGIN_URI = "login.htm";

	private String[] excludeUri = DEFAULT_EXCLUDE_URI;

	private String[] includeSuffixs = DEFAULT_INCLUDE_SUFFIXS;

	private String loginUri = DEFAULT_LOGIN_URI;

	private FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;

		String excludeUriStr = this.filterConfig.getInitParameter(EXCLUDE_URI);
		String incluedSuffixs = this.filterConfig
				.getInitParameter(INCLUDE_SUFFIXS_NAME);
		String loginUriName = this.filterConfig
				.getInitParameter(LOGIN_URI_NAME);

		if (StringUtils.isNotBlank(excludeUriStr)) {
			excludeUri = excludeUriStr.split(",");
		}

		if (StringUtils.isNotBlank(incluedSuffixs)) {
			this.includeSuffixs = incluedSuffixs.split(",");
			// 处理匹配字符串为".后缀名"
			for (int i = 0; i < includeSuffixs.length; i++) {
				includeSuffixs[i] = "." + includeSuffixs[i];
			}
		}

		if (StringUtils.isNotBlank(loginUriName)) {
			loginUri = loginUriName;
		}
	}

	@Override
	public void destroy() {
		filterConfig = null;
	}

	/**
	 * 当Session超时时，跳转至登录页面
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpservletrequest = (HttpServletRequest) request;
		String uriStr = httpservletrequest.getServletPath().toLowerCase();

		boolean needCheck = false;

		for (String suffix : includeSuffixs) {
			if (uriStr.endsWith(suffix)) {
				needCheck = true;
			}
		}

		if (needCheck) {
			for (String uri : excludeUri) {
				if (uriStr.indexOf(uri.toLowerCase()) > 0) {
					needCheck = false;
					break;
				}
			}
		}

		if (needCheck) {
			Object userId = httpservletrequest.getSession(true).getAttribute(
					"userId");
			if (userId == null || "".equals(userId.toString())) {
				String contPath = httpservletrequest.getContextPath()
						+ loginUri;
				StringBuffer sb = new StringBuffer();
				sb.append("<html>");
				sb.append("<script language='javascript'>");
				sb.append("window.top.open ('" + contPath + "','_top');");
				sb.append("</script>");
				sb.append("</html>");
				response.getWriter().println(sb.toString());
				response.getWriter().flush();
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}
