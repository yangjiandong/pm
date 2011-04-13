Personnel_Management System
============================

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
