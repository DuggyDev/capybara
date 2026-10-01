@echo off
where mvn >nul 2>nul
if errorlevel 1 (
  echo Maven is niet gevonden. Installeer JDK 25 + Maven eerst.
  pause
  exit /b 1
)
call mvn clean package
echo.
echo Klaar! Je jar staat in de map "target": CapybaraScoreboard-1.0.0.jar
pause
