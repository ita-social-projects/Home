package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LiquibaseUpdate {
    private Connection connection_for_db;
    //    name of references in migration.properties
    static final String PATHNAME = "changelog.path";
    final static String PROPERTYFILENAME = "migration";

    public LiquibaseUpdate(Connection connection_for_db) {
        this.connection_for_db = connection_for_db;
    }

    public void runLiquibase() throws LiquibaseException, SQLException {
        String path = getFromProperties(PROPERTYFILENAME, PATHNAME);
        Connection connection = connection_for_db;
        DriverManager.registerDriver(new Driver());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        CommandLineResourceAccessor commandLineResourceAccessor = new CommandLineResourceAccessor(classLoader);
        CompositeResourceAccessor compositeResourceAccessor = new CompositeResourceAccessor(commandLineResourceAccessor);
        Liquibase liquibase = new Liquibase(path, compositeResourceAccessor, new JdbcConnection(connection));
        liquibase.update(new Contexts());
    }

    //    Get value from migration.properties
    public static String getFromProperties(String propertyFileName, String propertyKey) {
        ResourceBundle rb = ResourceBundle.getBundle(propertyFileName);
        return rb.keySet().stream().filter(key -> key.equals(propertyKey))
                .map(key -> rb.getString(key))
                .findFirst().map(Object::toString).orElse("");
    }
}
