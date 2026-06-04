@echo off
cd /d %~dp0
echo Starting SpeedNote...
start "SpeedNote" python app.py
timeout /t 2 /nobreak >nul
start http://localhost:5000
echo Browser opened. Close the SpeedNote window to stop the server.
