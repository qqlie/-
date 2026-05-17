@echo off
set "JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
call mvnw.cmd spring-boot:run
