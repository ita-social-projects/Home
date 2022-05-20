@echo off
start /W heroku login
docker pull homebayraktar/home-application &&^
start /W docker tag homebayraktar/home-application registry.heroku.com/home-project-academy/web &&^
choice /c abcdefghijklmnopqrstuvwxyz0123456789 /n /m "Make sure that previous steps worked out properly and press any key to continue, or press q to exit."
if "%errorlevel%" == "17" exit
start /W heroku container:login
start /W docker push registry.heroku.com/home-project-academy/web
heroku container:release -a home-project-academy web &&^
TIMEOUT /T 5
