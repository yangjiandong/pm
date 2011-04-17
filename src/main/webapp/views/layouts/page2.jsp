<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page session="false" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
  <title>
    <tiles:insertAttribute name="title" />
  </title>
  <link rel="stylesheet" href="<c:url value="/resources/css/blueprint/screen.css" />" type="text/css" media="screen, projection" />
  <link rel="stylesheet" href="<c:url value="/resources/css/blueprint/print.css" />" type="text/css" media="print" />
  <!--[if lt IE 8]>
    <link rel="stylesheet" href="<c:url value="/resources/css/blueprint/ie.css" />" type="text/css" media="screen, projection" />
  <![endif]-->
  <tiles:useAttribute id="styles" name="styles" classname="java.util.List" ignore="true" />
  <c:forEach var="style" items="${styles}">
    <link rel="stylesheet" href="<c:url value="/resources/css/${style}" />" type="text/css" media="all" />
  </c:forEach>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery.min.js" />"></script>
  <tiles:useAttribute id="scripts" name="scripts" classname="java.util.List" ignore="true" />
  <c:forEach var="script" items="${scripts}">
    <script type="text/javascript" src="<c:url value="/resources/js/${script}" />"></script>
  </c:forEach>
  <link rel="shortcut icon" href="<c:url value="/resources/img/icon/extjs.ico" />" />

  <style>
  #logo h1 {
  margin-bottom: 0;
}

#navigation ul {
  margin-left: 0;
  list-style: none;
}

#navigation li {
  float: left;
}

#appointmentNavigation a {
  margin-right: 3px;
}

  </style>
</head>
<body>
  <div id="page" class="container">
    <div id="header" class="span-24 last">
      <div id="topbar" class="span-24 last">
        <p>
          <c:choose>
            <c:when test="${pageContext.request.userPrincipal != null}">
              ${pageContext.request.userPrincipal.name} | <a href="<c:url value="/users/signout" />">Sign Out</a>
            </c:when>
            <c:otherwise>
              <a href="<c:url value="/users/signin" />">Sign In</a>
            </c:otherwise>
          </c:choose>
        </p>
      </div>
      <hr/>
      <div id="logo" class="span-24 last">
        <div class="span-6">
          <h1>Petcare</h1>
          <h2 class="alt">We love your pet</h2>
        </div>
        <div id="navigation" class="span-18 last">
          <ul>
            <li><a class="button" href="<c:url value="/" />">Home</a></li>
            <li><a class="button" href="<c:url value="/appointments" />">Appointments</a></li>
          </ul>
        </div>
      </div>
      <hr/>
    </div>
    <div id="content" class="span-24">
      <tiles:insertAttribute name="content" />
    </div>
    <hr/>
    <div id="footer" class="span-24">
      Copyright (c) 2010 SpringSource | About
    </div>
  </div>
</body>
</html>