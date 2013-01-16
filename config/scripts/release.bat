@echo off
SET ROOT_PATH=../../
SET APP_NAME=%0
SET ARG1=%1
SET ARG2=%2

call :getargc ARGS_COUNT %*

if "%ARG1%" == "" (
	call :release %ROOT_PATH%/utils
	call :release %ROOT_PATH%/spring-util
	call :release %ROOT_PATH%/templater
	call :release %ROOT_PATH%/freemarker-templater
	call :release %ROOT_PATH%/velocity-templater
	call :release %ROOT_PATH%/commons-dal
	call :release %ROOT_PATH%/hibernate-dal
	call :release %ROOT_PATH%/test-hibernate-dal
	call :release %ROOT_PATH%/mail-sender
	call :release %ROOT_PATH%/mail-hibernate
	call :release %ROOT_PATH%/i18n-hibernate
	call :release %ROOT_PATH%/parameter-hibernate
	call :release %ROOT_PATH%/oxm
	call :release %ROOT_PATH%/xslt
) else if "%ARG2%" == "" (		
	if exist %ROOT_PATH%/%ARG1% (		
		call :release %ROOT_PATH%/%ARG1%
	) else (
		echo %ROOT_PATH%/%ARG1% n'est pas un repertoire valide
		echo.
		call :help %APP_NAME%		
	)
) else (
	if "%ARG1%" == "-s" (		
		call :releaseFrom %ARG2%
	) else (		
		call :help %APP_NAME%
	)		
) 
exit /b

:help
SET APP_NAME=%~1
echo %APP_NAME% [-s nomDuProjet] ^| [nomDuProjet] ^| []
echo.
echo 	%APP_NAME%			Effectue la release complete des projets
echo 	%APP_NAME% nomDuProjet		Effectue la release du projet uniquement
echo		%APP_NAME% -s nomDuProjet	Demarre la release a partir du projet 
goto :EOF

:release
SET DIR=%~1/trunk
call mvn -f %DIR%/pom.xml clean release:prepare
call mvn -f %DIR%/pom.xml release:perform -Dgoals=install
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b
goto :EOF


:releaseFrom
SET PROJECT=%~1
if exist %ROOT_PATH%/%PROJECT% (
	call :release %ROOT_PATH%/%PROJECT%
	if %PROJECT% == utils (		
		call :releaseFrom spring-util
	) else if %PROJECT% == spring-util (
		call :releaseFrom templater
	    call :releaseFrom commons-dal
		call :releaseFrom mail-sender
		call :releaseFrom oxm
		call :releaseFrom xslt
	) else if %PROJECT% == templater (
		call :releaseFrom freemarker-templater
		call :releaseFrom velocity-templater			
	) else if %PROJECT% == commons-dal (
		call :releaseFrom hibernate-dal	
	) else if %PROJECT% == hibernate-dal (
		call :releaseFrom test-hibernate-dal
		call :releaseFrom mail-hibernate
		call :releaseFrom i18n-hibernate
		call :releaseFrom parameter-hibernate
	)	
)
goto :EOF

:getargc
set getargc_v0=%1
set /a "%getargc_v0% = 0"

:getargc_l0
if not x%2x==xx (
	shift
	set /a "%getargc_v0% = %getargc_v0% + 1"
	goto :getargc_l0
)
set getargc_v0=
goto :eof
