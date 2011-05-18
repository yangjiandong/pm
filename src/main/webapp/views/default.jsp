<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="X-UA-Compatible" content="chrome=1" id="chrome" />
<link rel="shortcut icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />
<link rel="icon" href="<c:url value="/resources/img/icon/favicon.ico"/>" />

<link rel="stylesheet"
  href="<c:url value='/resources/ext/resources/css/ext-all.css'/>"
  type="text/css" />
<link rel="stylesheet" href="<c:url value='/resources/css/app.css'/>"
  type="text/css" />
<link rel="stylesheet" href="<c:url value='/resources/css/ext_icon.css'/>"
  type="text/css" />
<% //if client_browser_name == 'Mozilla'%>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-patch.css"/>" />
<% //end %>

<script type="text/javascript" src="<c:url value='/resources/ext/ext-base-debug.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/ext/ext-all-debug.js'/>"></script>

<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-basex.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/ext/ext-lang-zh_CN.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/app.base.grid.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/app.base.edit.grid.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/app.base.form.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/utils.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/lib/tdgi.borderLayout.js'/>"></script>

<script type="text/javascript"
  src="<c:url value='/resources/js/app/sys/change_pwd_win.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/about.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/main.js'/>"></script>
<script type="text/javascript"
  src="<c:url value='/resources/js/include.maps.js'/>"></script>

<script type="text/javascript">
      Ext.BLANK_IMAGE_URL = '${ext}/resources/images/default/s.gif';

      // Deployment type: Production(PROD) or development(DEV). In development mod does not cache
      CFG_DEPLOYMENT_TYPE = '${run_mode}';
      //javascript
      CFG_PATH_EXTJS = '${ext}';
      CFG_PATH_JSLIB = 'resources/js/lib';
      CFG_PATH_ICONS = 'resources/img';
</script>

<title>${application_name }</title>
</head>
<body>
  <div id="menubar"></div>
  <div id="west"></div>
  <div id="south"></div>
</body>
</html>



