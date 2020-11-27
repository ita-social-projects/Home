package com.softserveinc.ita.homeproject.migrations;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LiquibaseUpdate {
//    name of references in migration.properties
    private static final String DB_HOST = "url";
    private static final String PATHNAME = "changelog.path";
    private static final String CHANGELOG = "changelog.name";

    public void runLiquibase(String user, String password) {
        String url = getFromProperties(DB_HOST);
        String path = getFromProperties(PATHNAME);
        String changelogName = getFromProperties(CHANGELOG);
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            FileSystemResourceAccessor fileSystemResourceAccessor = new FileSystemResourceAccessor();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            CommandLineResourceAccessor commandLineResourceAccessor = new CommandLineResourceAccessor(classLoader);
            CompositeResourceAccessor compositeResourceAccessor = new CompositeResourceAccessor(fileSystemResourceAccessor, commandLineResourceAccessor);
            try (Liquibase liquibase = new Liquibase(path+changelogName, compositeResourceAccessor, database)) {
                liquibase.update("first_migration");
            } catch (LiquibaseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    Get value from migration.properties
    public String getFromProperties(String value) {
        String url = "";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("migration.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value
            url = prop.getProperty(value);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return url;
    }
}
