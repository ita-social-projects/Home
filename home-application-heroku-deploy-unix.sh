#!/bin/bash

heroku login
docker pull homeacademy/home-application
docker tag homeacademy/home-application registry.heroku.com/home-project-academy/web
heroku container:login
docker push registry.heroku.com/home-project-academy/web
heroku container:release -a home-project-academy web
echo SUCCESS
sleep 5
