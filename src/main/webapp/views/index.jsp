<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%pageContext.setAttribute("ext", "resources/ext");%>
<%pageContext.setAttribute("deployment_type", "DEV");%>
<%pageContext.setAttribute("apath", basePath);%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="${apath}/resources/img/icon/extjs.ico" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext/resources/css/ext-all.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-customer.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-patch.css"/>" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/xtheme-nbs.css"/>" />

<script type="text/javascript" src="<c:url value="/resources/ext/ext-base.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext/ext-lang-zh_CN.js"/>"></script>

<script type="text/javascript">
      Ext.BLANK_IMAGE_URL = '${ext}/resources/images/default/s.gif';

      // Deployment type: Production(PROD) or development(DEV). In development mod does not cache
      CFG_DEPLOYMENT_TYPE = '${deployment_type}';
      //javascript
      CFG_PATH_EXTJS = '${ext}';
      CFG_PATH_JSLIB = 'resources/js/lib';
      CFG_PATH_ICONS = 'resources/img';
</script>

<script type="text/javascript"  src="<c:url value="/resources/js/labels_srv.js"/>" ></script>
<script type="text/javascript">
      // Product version. Do not change!
      CFG_PRODUCT_VERSION = L("/Application/Version");
      CFG_DEFAULT_LANGUAGE = 'en';
      CFG_AUTHSERVER_URL = 'j_spring_security_check';
      CFG_CLIENT_URL =  'resources/js/app';
      CFG_EXT_3RD = 'resources/js/3rdparty';

</script>
<script type="text/javascript" src="<c:url value="/resources/js/app/UiLoadingHelper.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/DcIncludesMap.js"/>"></script>

<script>
      document.write('<title>' + L("/Application/Name")+'-'+ CFG_PRODUCT_VERSION + '</title>');
</script>
</head>
<body>
<div id="app-loading-mask" style=""></div>
<div id="app-loading">
<div class="app-loading-indicator">
<span id="app-loading-logo">
<script>
      s=L("/Application/Name")+'-'+ CFG_PRODUCT_VERSION;

      document.write(s);
</script>
</span><br>
<span id="app-loading-logo-text"></span> <br>
<img src="resources/img/extanim32.gif" /> <br>
<span id="app-loading-msg">装载中...</span></div>
</div>

<div id="west"></div>
<div id="north" style="height: 32"></div>
<div id="south" style="margin: 0; padding: 0;"></div>
<div style="width: 100%; height: 100%; overflow: hidden;" id="content">
<iframe id="content_iframe" src=""
  style="border: 0; width: 100%; height: 100%; overflow-y: hidden; margin: 0; padding: 0;"
  FRAMEBORDER="no"></iframe></div>

<script type="text/javascript">document.getElementById('app-loading-msg').innerHTML = '装载资源...';</script>
<script type="text/javascript" src="<c:url value='/resources/js/lib/App.base.Component.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/ux/Ext.ux.Image.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/lib/lib.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/app/DcLogin.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/app/DcMenuTree.js'/>"></script>
<script type="text/javascript">document.getElementById('app-loading-msg').innerHTML = '初始化...';</script>
<!--
<script type="text/javascript" src="<c:url value='/resources/js/main.js' />"></script>
 -->
<div id="header"><img alt="logo" src="resources/img/log-line.gif" /></div>
<div id="footer"><p>版权信息</p></div>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/app.css"/>" />
<script type="text/javascript" src="<c:url value='/resources/js/opoa.main.js' />"></script>
</body>
</html>