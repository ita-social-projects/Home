## Deploy workflow to Heroku

### Warning
- If you have `GIT Bash`  or another bash interpreter installed, you should
use the `home-application-heroku-deploy-unix.sh` script.

- **After execution make sure that
[application on Heroku](https://home-project-academy.herokuapp.com/api/0/apidocs/index.html)
works properly!**

### Prerequisites:

- [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli#install-with-an-installer)
- You have credentials for Heroku
- **CI passed**

### How to use: 

#### For Unix systems and Windows with Bash interpreter:

1) Execute the `home-application-heroku-deploy-unix.sh` and follow the 
instructions to log in to Heroku.
4) When you'll see `Logged in {email}` message type `Ctrl + C` for Windows
/ Linux or `Cmd + C` for macos. *Do not* terminate the process with
   next choice option if you logged in successfully.
5) Wait for execution to finish without error messages.
6) After execution, the script will end automatically.

#### For Windows machines without bash interpreter:

1) Execute the `home-application-heroku-deploy-windows.bat` and follow
the instructions.
2) Close prompt with login window after end of process.
3) *Do not* terminate the process if previous step was successful.
4) Follow the instructions
5) Close Login window and *do not* terminate the process if previous step was
successful
6) After execution, the script will end automatically.
