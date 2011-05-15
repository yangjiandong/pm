Personnel_Management System
============================

2011.05.15
----------

   1. Tomcat的gzip压缩功能

 <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443"
         compression="on"
               compressionMinSize="2048"
               noCompressionUserAgents="gozilla, traviata"
               compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain"  />

具体参数说明如下：

1) compression="on" 打开压缩功能
2) compressionMinSize="2048" 启用压缩的输出内容大小，这里面默认为2KB
3) noCompressionUserAgents="gozilla, traviata" 对于以下的浏览器，不启用压缩
4) compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain"　压缩类型

   2. nan21 java
   https://github.com/nan21/dnet


2011.04.27
----------

   1. 自定义数据类型
   usertype.ArrayType

   2. add hibernate-search
   http://nopainnogain.iteye.com/blog/859731

    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-search</artifactId>
    </dependency>
    <!-- lucene start -->
    <dependency>
      <groupId>org.apache.lucene</groupId>
      <artifactId>lucene-analyzers</artifactId>
      <version>2.4.0</version>
    </dependency>
    <dependency>
      <groupId>org.apache.lucene</groupId>
      <artifactId>lucene-highlighter</artifactId>
      <version>2.4.0</version>
    </dependency>

    org.ssh.pm.common.entity.Employee

2011.04.24
----------

   1. save/jdbcTemplate.txt

   2. org.ssh.pm.common.web.UserSession
      org.ssh.pm.common.web.PageUtils

   3. jfreechartTemplate
   org.ssh.pm.utils

2011.04.23
----------

   1. test default.jsp
  <!--
  <definition name="index" template="/views/index.jsp"/>
  -->

  <!-- init data -->
  <definition name="index" extends="page">
    <put-attribute name="title" value="初始化数据..." type="string" />
    <put-attribute name="content" value="/views/default.jsp" type="template" />
  </definition>

   2. 通过反射Hibernate实体字段名
   http://shewolfep.iteye.com/blog/1013390
   HibernateTool

2011.04.20
----------

   1. 多数据源(相同表存放多个数据库   )
   applicationContext-dynamic-datasource.xml

2011.04.18
----------

   1. docbook mvn plugin

   参考 netty 项目
   mvn org.jboss.maven.plugins:maven-jdocbook-plugin:2.2.0:resources org.jboss.maven.plugins:maven-jdocbook-plugin:2.2.0:generate
   or
   mvn jdocbook:resources jdocbook:generate

   中文问题:
   maven-jdocbook-plugin

2011.04.17
----------

   1. 存储过程

   CommonController -> AccountManager -> UserJdbcDao
   curl http://localhost:8080/sshapp/common/getAllUser

   不采用model对应直接转换json格式

2011.04.13
----------

   1. IdEntity -> UIdEntity
   原因看save/q.txt - 2.

   2. 增加 db log 功能,把用户访问,特别设置的信息保存到db log
   <import resource="log/applicationContext-log.xml" />
   log4j.properties
   #Async Database Appender (Store business message)
   log4j.appender.DB=org.ssh.pm.log.appender.QueueAppender
   log4j.appender.DB.QueueName=dblog

   /**
   * 在log4j.properties中,本logger已被指定使用asyncAppender.
   */
  public static final String DB_LOGGER_NAME = "DBLogExample";

  example: LogAction
  注意,配置batchSize,需缓存量超过batchSize才提交到后台表

2011.04.12
----------

   1. tag

   git tag -a tag_20110412
   git archive --format=tar --remote=[hostname]:[path to repo] [tag name] > tagged_version.tar

   --推送到remote
   git push origin spring3.02

   --或全部tag
   git push origin --tags

   2. tomcat plugin + jrebel

   -Drebel.log=true -noverify -javaagent:c:\jrebel.jar -Xmx512M -Xms512M -XX:MaxPermSize=1024m

   3. 统一采用 tomcat 6.0.18
    <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>jasper-el</artifactId>
        <version>6.0.18</version>
    </dependency>

    use "jasper-el-6.0.18.jar",exclude  "el-api-6.0.18.jar"
    <packagingExcludes>WEB-INF/lib/el-*</packagingExcludes>
    <warSourceExcludes>WEB-INF/lib/el-*</warSourceExcludes>

    方便jetty \ tomcat 切换
    TODO 暂时能运行

    mvn war:inplace 运行后 需手工删除 el-api-6.0.18.jar

    4. 关闭memcached,先采用ecache
    <import resource="cache/applicationContext-memcached.xml" />

    CacheUtil 中定义cacheName,HZK,COMMON
    测试 HzService.getMemo
    curl http://localhost:8080/sshapp/book/getHz

    method Cached:
    需配合

  <bean id="myBean" class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target">
      <bean class="org.ssh.app.example.service.BookService" />
    </property>
    <property name="interceptorNames">
      <list>
        <value>methodCachePointCut</value>
      </list>
    </property>
  </bean>

2011.04.11
-----------

   1. 手工建立eclipse项目

   a、建立m2_home变量
     mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo
   b、生成eclipse项目
     mvn eclipse:eclipse
     (mvn eclipse:configure-workspace eclipse:eclipse -Declipse.workspace=../workspace)
     bin/eclipse.bat

   2. git remote

   mkdir pm
   cd pm
   git init
   touch README
   git add README
   git commit -m 'first commit'
   git remote add origin git@github.com:yangjiandong/pm.git
   git push -u origin master

   Existing Git Repo?
   cd existing_git_repo
   git remote add origin git@github.com:yangjiandong/pm.git
   git push -u origin master

   3. use tomcat plugin (use jrebel)

   修改tomcat的server.xml文件，加入

     <Context docBase="E:\workspace\petclinic\src\main\webapp" path="/petclinic" reloadable="false"></Context>

   右键单击项目，选择build path，将default output folder设置为 petclinic/src/main/webapp/WEB-INF/classes，并将
   petclinic/src/main/resource的Excluded设置为None，默认是**，意思是让eclipse编译java和resource文件编译到petclinic/src/main/webapp/WEB-INF/classes目录
   (以上步骤不再需要,pom.xml中已定义)

   运行 mvn war:inplace
   petclinic/src/main/webapp/WEB-INF下，就有lib文件了

   --END
