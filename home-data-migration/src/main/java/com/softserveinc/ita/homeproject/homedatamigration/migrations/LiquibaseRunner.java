package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.exception.LiquibaseException;

import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseRunner {
    public static void main(String[] args) throws LiquibaseException, SQLException {
        // Full url to connecting to Db on Heroku
        // String urljdbc = "jdbc:postgresql://ec2-34-254-24-116.eu-west-1.compute.amazonaws.com:5432/d1pi5jhrf0fbun?user=hwkbjsunlfuchw&password=c9039cc29d3150b2288c58d1eb988c7ea9a3a5b38eb117f39b5cb9bbf6fccd76";
        String user = LiquibaseUpdate.getFromProperties(LiquibaseUpdate.PROPERTYFILENAME,
                "user");
        String pass = LiquibaseUpdate.getFromProperties(LiquibaseUpdate.PROPERTYFILENAME,
                "password");
        String url = LiquibaseUpdate.getFromProperties(LiquibaseUpdate.PROPERTYFILENAME,
                "url");
        boolean checkFullUrl = false;
        // read args from command line
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                i++;
                pass = args[i];
            } else if (args[i].equals("-user")) {
                i++;
                user = args[i];
            } else if (args[i].equals("-url")) {
                i++;
                url = args[i];
                // full url with password and user
            } else if (args[i].equals("-furl")) {
                i++;
                url = args[i];
                checkFullUrl = true;
            }
        }
        LiquibaseUpdate liquibaseUpdate = setParam(url, user, pass, checkFullUrl);
        liquibaseUpdate.runLiquibase();
    }
    private static LiquibaseUpdate setParam(String url, String user, String password, boolean check) throws SQLException {
        LiquibaseUpdate liquibaseUpdate;
        if (check) {
            liquibaseUpdate = new LiquibaseUpdate(DriverManager.getConnection(url));
        } else {
            liquibaseUpdate = new LiquibaseUpdate(DriverManager.getConnection(url, user, password));
        }
        return liquibaseUpdate;
    }
}
