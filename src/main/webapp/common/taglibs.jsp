<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s"  uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%@page import="org.ssh.pm.common.web.PageUtils"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
      + path + "/";

  pageContext.setAttribute("application_name", (String)PageUtils.getApplicationInfos().get("application_name"));
  pageContext.setAttribute("version", (String)PageUtils.getApplicationInfos().get("version"));
  pageContext.setAttribute("ext", "resources/ext");
  pageContext.setAttribute("run_mode", (String)PageUtils.getApplicationInfos().get("run_mode"));
  pageContext.setAttribute("apath", basePath);
  pageContext.setAttribute("copyright", (String)PageUtils.getApplicationInfos().get("copyright"));
%>
