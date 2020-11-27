package com.softserveinc.ita.homeproject.migrations;

import java.util.Scanner;

public class LiquibaseRunner {
    public static void main(String[] args) {
        String pass, user;
        Scanner scanner = new Scanner(System.in);
        user = scanner.nextLine();
        pass = scanner.nextLine();
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.runLiquibase(user, pass);
    }
}
