package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;
import java.sql.Connection;
import java.sql.DriverManager;


@Slf4j
public class LiquibaseUpdate {
    private String test1;
    private String test2;

    private static final String PATH = "db/changelog/master.xml";

    private Connection connection;

    public LiquibaseUpdate(Connection connection) {
        this.connection = connection;
    }

    public void runLiquibase() throws Exception {
        System.out.println(test2);
        DriverManager.registerDriver(new Driver());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        CommandLineResourceAccessor resourceAccessor = new CommandLineResourceAccessor(classLoader);
        JdbcConnection jdbcConnection = new JdbcConnection(this.connection);
        try (Liquibase liquibase = new Liquibase(PATH, resourceAccessor, jdbcConnection)) {
            liquibase.update(new Contexts());
        } catch (Exception e) {

        }
    }
}
