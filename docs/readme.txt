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


   --END