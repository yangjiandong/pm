@echo off
echo [INFO] run Hibernate instrumentTask  do bytecode enhancement .

cd ../../
call mvn process-classes
cd bin
pause