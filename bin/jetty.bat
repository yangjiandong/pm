@echo off
echo [INFO] Use maven jetty-plugin run the project.

cd %~dp0
cd ..
set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
call mvn -o jetty:run -Dmaven.test.skip=true
cd bin
pause