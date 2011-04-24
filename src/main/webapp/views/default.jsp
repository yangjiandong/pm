<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<style>
/* leftbar */
#leftbar a {
  display: block;
  padding: 0.3em;
  padding-right: 0;
  border-bottom: 1px dashed #eeeeee;
  font-weight: bold;
  text-decoration: none;
  font-weight: bold;
}

#leftbar a:hover {
  color: #2a65ad;
  background-color: #eeeeee;
}

.login {
  padding: 0.8em;
  margin-bottom: 1em;
  border: 2px solid #ddd;
}

.login {
  background: #d5edf8;
  color: #205791;
  border-color: #92cae4;
}
</style>
<div class="container">

  <div id="leftbar" class="span-12">

    <a href="${ctx}/">首页</a> <a href="#">综合演示</a> <a
      href="${ctx}/jms/index.action">JMS演示</a> <a
      href="${ctx}/jmx/jmx-client.action">JMX演示</a> <a
      href="${ctx}/email/index.action">邮件演示</a> <a
      href="${ctx}/ajax/index.action">Ajax演示</a> <a
      href="${ctx}/report/index.action">报表演示</a> <a
      href="${ctx}/cache/index.action">Cache演示</a> <a
      href="${ctx}/schedule/index.action">定时任务演示</a> <a
      href="${ctx}/xml_json/index.action">XML/JSON操作演示</a> <a
      href="${ctx}/security/index.action">安全高级演示</a> <a
      href="${ctx}/log/index.action">日志高级演示</a> <a
      href="${ctx}/web/index.action">Web高级演示</a> <a
      href="${ctx}/orm/index.action">数据库高级演示</a> <a
      href="${ctx}/webservice/index.action">Web服务高级演示</a> <a
      href="${ctx}/j_spring_security_logout">退出登录</a>

  </div>
  <div class="span-12 last">
    <div class="error">
      This is a &lt;div&gt; with the class <strong>.error</strong>. <a
        href="#">Link</a>.
    </div>
    <div class="box">
      <fieldset>
        <legend>Select, checkboxes, lists</legend>
        <p>
          <label for="dummy0">Text input (title)</label><br> <input
            type="text" class="title" name="dummy0" id="dummy0"
            value="Field with class .title">
        </p>
        <p>
          <label for="dummy3">A password field</label><br> <input
            type="password" class="text" id="dummy3" name="dummy3"
            value="Password field with class .text">
        </p>

        <p>
          <input type="submit" value="Submit"> <input type="reset"
            value="Reset">
        </p>
      </fieldset>
    </div>

  </div>
</div>