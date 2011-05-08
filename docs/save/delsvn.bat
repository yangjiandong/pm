@echo On
@Rem 删除SVN版本控制目录
@PROMPT [Com]#

@for /r . %%a in (.) do @if exist "%%a\.svn" rd /s /q "%%a\.svn"
@Rem for /r . %%a in (.) do @if exist "%%a\.svn" @echo "%%a\.svn"

@echo Mission Completed.
@pause