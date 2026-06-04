@echo off
cd /d %~dp0
echo SpeedNote を起動中...
start /B python app.py
timeout /t 2 /nobreak >nul
start http://localhost:5000
echo ブラウザを開きました。サーバーを止めるには Ctrl+C を押してください。
