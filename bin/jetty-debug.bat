@echo off
echo [INFO] Use maven jetty-plugin run the project, with debug options.

cd %~dp0
cd ..
set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
call mvn -o jetty:run -Dmaven.test.skip=true
cd bin
pause
