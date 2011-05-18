<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

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
<script type="text/javascript" src="<c:url value='/resources/ext/ext-lang-zh_CN.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/login.js'/>"></script>
<script type="text/javascript">
      Ext.BLANK_IMAGE_URL = '${ext}/resources/images/default/s.gif';

      // Deployment type: Production(PROD) or development(DEV). In development mod does not cache
      CFG_DEPLOYMENT_TYPE = '${run_mode}';
      //javascript
      CFG_PATH_EXTJS = '${ext}';
      CFG_PATH_JSLIB = 'resources/js/lib';
      CFG_PATH_ICONS = 'resources/img';
</script>

<title>${application_name}</title>
</head>
<body>

<div id="login-main"></div>
</body>

</html>