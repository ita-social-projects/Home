package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import org.postgresql.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseUpdate {

    private final String PATH = "db/changelog/db.changelog-master.xml";

    public void runLiquibase(Connection connection) throws LiquibaseException, SQLException {
        DriverManager.registerDriver(new Driver());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Liquibase liquibase = new Liquibase(PATH, new CommandLineResourceAccessor(classLoader),
                new JdbcConnection(connection));
        liquibase.update(new Contexts());
    }
}
