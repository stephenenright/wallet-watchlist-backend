@echo off
setlocal

set SCRIPT_DIR=%~dp0
set JAVA_HOME=%SCRIPT_DIR%jre
set JAVA=%JAVA_HOME%\bin\java.exe

"%JAVA%" -jar "%SCRIPT_DIR%app.jar" %*
