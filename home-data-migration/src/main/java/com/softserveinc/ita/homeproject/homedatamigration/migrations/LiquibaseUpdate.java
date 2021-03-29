package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;


@Slf4j
public class LiquibaseUpdate {

    private static final String PATH = "db/changelog/master.xml";

    private Connection connection;

    public LiquibaseUpdate(Connection connection) {
        this.connection = connection;
    }

    public void runLiquibase() throws SQLException, LiquibaseException {
        DriverManager.registerDriver(new Driver());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        CommandLineResourceAccessor resourceAccessor = new CommandLineResourceAccessor(classLoader);
        JdbcConnection jdbcConnection = new JdbcConnection(this.connection);
        try (Liquibase liquibase = new Liquibase(PATH, resourceAccessor, jdbcConnection)) {
            liquibase.update(new Contexts());
        } catch (Exception e) {
            log.error("Running liquibase updating failed", e);
            throw e;
        }
    }
}
