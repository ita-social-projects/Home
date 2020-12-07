package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

public class LiquibaseRunner {
    public static void main(String[] args) throws LiquibaseException, SQLException {
        //default value
        String url = "jdbc:postgresql://localhost:5432/test";
        String user = "postgres";
        String pass = "1234";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                i++;
                pass = args[i];
            } else if (args[i].equals("-us")) {
                i++;
                user = args[i];
            } else if (args[i].equals("-u")) {
                i++;
                url = args[i];
            }
        }
        LiquibaseUpdate liquibaseUpdate = LiquibaseUpdate.newBuilder()
                .setUrl(url)
                .setUser(user)
                .setPassword(pass).build();
        liquibaseUpdate.runLiquibase();
    }
}
