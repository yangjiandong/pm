<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%pageContext.setAttribute("Application_Name", "应用程序开发框架");%>
<%pageContext.setAttribute("version", "1.0-snopp");%>
<%pageContext.setAttribute("ext", "resources/ext");%>
<%pageContext.setAttribute("run_mode", "DEV");%>
<%pageContext.setAttribute("apath", basePath);%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<!--
<link rel="shortcut icon" href="${apath}resources/img/icon/extjs.ico" />
 -->
<link rel="shortcut icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />
<link rel="icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext/resources/css/ext-all.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/app.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext_icon.css"/>" />
<% //if client_browser_name == 'Mozilla'%>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-patch.css"/>" />
<% //end %>

<script type="text/javascript" src="<c:url value="/resources/ext/ext-base.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext/ext-lang-zh_CN.js"/>"></script>

<script type="text/javascript">
      Ext.BLANK_IMAGE_URL = '${ext}/resources/images/default/s.gif';

      // Deployment type: Production(PROD) or development(DEV). In development mod does not cache
      CFG_DEPLOYMENT_TYPE = '${run_mode}';
      //javascript
      CFG_PATH_EXTJS = '${ext}';
      CFG_PATH_JSLIB = 'resources/js/lib';
      CFG_PATH_ICONS = 'resources/img';
</script>

<script type="text/javascript"  src="<c:url value="/resources/js/labels_srv.js"/>" ></script>
<script type="text/javascript">
      // Product version. Do not change!
      CFG_PRODUCT_VERSION = '${version}';
      CFG_DEFAULT_LANGUAGE = 'en';
      CFG_AUTHSERVER_URL = 'j_spring_security_check';
      CFG_CLIENT_URL =  'resources/js/app';
      CFG_EXT_3RD = 'resources/js/3rdparty';

</script>
<script type="text/javascript" src="<c:url value="/resources/js/app/load.helper.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/include.maps.js"/>"></script>

<script>
      document.write('<title>' + '${Application_Name}'+'-'+ CFG_PRODUCT_VERSION + '</title>');
</script>
</head>
<body scroll="no" id="docs">
<div id="app-loading-mask"></div>
<div id="app-loading">
    <div class="app-loading-indicator">
        <img src="resources/img/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle" />
        资源装载中&hellip;
    </div>
</div>

<div id="west"></div>
<div id="north" style="height: 32"></div>
<div id="south" style="margin: 0; padding: 0;"></div>
<div style="width: 100%; height: 100%; overflow: hidden;" id="content">
<iframe id="content_iframe" src=""
  style="border: 0; width: 100%; height: 100%; overflow-y: hidden; margin: 0; padding: 0;"
  FRAMEBORDER="no"></iframe></div>

<script type="text/javascript" src="<c:url value='/resources/js/lib/app.base.component.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/ux/Ext.ux.Image.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/lib/lib.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/app/DcLogin.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/app/DcMenuTree.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/main.js' />"></script>

</body>
</html>