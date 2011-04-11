Personnel_Management System
============================

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