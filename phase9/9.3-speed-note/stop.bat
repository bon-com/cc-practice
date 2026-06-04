@echo off
echo Stopping SpeedNote...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :5000') do (
    taskkill /PID %%a /F >nul 2>&1
)
echo Done.
pause
