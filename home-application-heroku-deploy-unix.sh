#!/bin/bash

heroku login
docker pull homebayraktar/home-application
docker pull homebayraktar/home-oauth-server
docker tag homebayraktar/home-application registry.heroku.com/home-project-academy/web
docker tag homebayraktar/home-oauth-server registry.heroku.com/home-oauth-server/web
heroku container:login
docker push registry.heroku.com/home-project-academy/web
heroku container:release -a home-project-academy web
docker push registry.heroku.com/home-oauth-server/web
heroku container:release -a home-oauth-server web
echo SUCCESS
sleep 5
