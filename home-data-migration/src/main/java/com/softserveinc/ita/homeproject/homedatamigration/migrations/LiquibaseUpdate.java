package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import org.postgresql.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseUpdate {

    final static String PATH = "db/changelog/db.changelog-master.xml";

    public void runLiquibase(Connection connection) throws LiquibaseException, SQLException {
        DriverManager.registerDriver(new Driver());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        CommandLineResourceAccessor commandLineResourceAccessor = new CommandLineResourceAccessor(classLoader);
        CompositeResourceAccessor compositeResourceAccessor = new CompositeResourceAccessor(commandLineResourceAccessor);
        Liquibase liquibase = new Liquibase(PATH, compositeResourceAccessor, new JdbcConnection(connection));
        liquibase.update(new Contexts());
    }
}
