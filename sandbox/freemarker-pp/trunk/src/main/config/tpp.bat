@echo off

set SCRIPT_DIR=%~dp0
set HOME=%SCRIPT_DIR%..
rem --------------------------------
rem Collect cmd line args
rem --------------------------------
set CMD_LINE_ARGS=%1
if ""%1""=="""" goto endArgs
:nextArg
shift
if ""%1"" == """" goto endArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
goto nextArg
:endArgs

java -jar %HOME%/build/${project.name}-${project.version}.jar %CMD_LINE_ARGS%
