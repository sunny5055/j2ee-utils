@echo off
SET ROOT_PATH=../../
SET APP_NAME=%0
SET ARG1=%1
SET ARG2=%2

call :getargc ARGS_COUNT %*

if "%ARG1%" == "" (
	call :loop %ROOT_PATH%/maven-pom-parent
	call :loop %ROOT_PATH%/utils
	call :loop %ROOT_PATH%/spring-util
	call :loop %ROOT_PATH%/templater
	call :loop %ROOT_PATH%/freemarker-templater
	call :loop %ROOT_PATH%/velocity-templater
	call :loop %ROOT_PATH%/commons-dal
	call :loop %ROOT_PATH%/hibernate-dal
	call :loop %ROOT_PATH%/test-hibernate-dal
	call :loop %ROOT_PATH%/mail-sender
	call :loop %ROOT_PATH%/mail-hibernate
	call :loop %ROOT_PATH%/i18n-hibernate
	call :loop %ROOT_PATH%/parameter-hibernate
	call :loop %ROOT_PATH%/oxm
	call :loop %ROOT_PATH%/xslt
) else if "%ARG2%" == "" (		
	if exist %ROOT_PATH%/%ARG1% (		
		call :loop %ROOT_PATH%/%ARG1%
	) else (
		echo %ROOT_PATH%/%ARG1% n'est pas un repertoire valide
		echo.
		call :help %APP_NAME%		
	)
) else (
	if "%ARG1%" == "-s" (		
		call :installFrom %ARG2%
	) else (		
		call :help %APP_NAME%
	)		
) 
exit /b

:help
SET APP_NAME=%~1
echo %APP_NAME% [-s nomDuProjet] ^| [nomDuProjet] ^| []
echo.
echo 	%APP_NAME%			Effectue l'installation complete des projets
echo 	%APP_NAME% nomDuProjet		Effectue l'installation du projet uniquement
echo		%APP_NAME% -s nomDuProjet	Demarre l'installation a partir du projet 
goto :EOF

:loop
SET DIR=%~1
for /d %%s in (%DIR%/tags/*) do call :install %DIR%/tags %%s
goto :EOF

:install
SET DIR=%~1/%~2
call mvn -f %DIR%/pom.xml clean javadoc:jar source:jar install clean
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" exit /b
goto :EOF


:installFrom
SET PROJECT=%~1
if exist %ROOT_PATH%/%PROJECT% (
	call :loop %ROOT_PATH%/%PROJECT%
	if %PROJECT% == utils (		
		call :installFrom spring-util
	) else if %PROJECT% == spring-util (
		call :installFrom templater
	    call :installFrom commons-dal
		call :installFrom mail-sender
		call :installFrom oxm
		call :installFrom xslt
	) else if %PROJECT% == templater (
		call :installFrom freemarker-templater
		call :installFrom velocity-templater			
	) else if %PROJECT% == commons-dal (
		call :installFrom hibernate-dal	
	) else if %PROJECT% == hibernate-dal (
		call :installFrom test-hibernate-dal
		call :installFrom mail-hibernate
		call :installFrom i18n-hibernate
		call :installFrom parameter-hibernate
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
