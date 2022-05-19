#!/bin/bash

heroku login
docker pull homeacademy/home-application
docker pull homeacademy/home-oauth-server
docker tag homeacademy/home-application registry.heroku.com/home-project-academy/web
docker tag homeacademy/home-oauth-server registry.heroku.com/home-oauth-server/web
heroku container:login
docker push registry.heroku.com/home-project-academy/web
heroku container:release -a home-project-academy web
docker push registry.heroku.com/home-oauth-server/web
heroku container:release -a home-oauth-server web
echo SUCCESS
sleep 5
