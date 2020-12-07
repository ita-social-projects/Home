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
    private String url;
    private String user;
    private String password;
    //    name of references in migration.properties
    private static final String PATHNAME = "changelog.path";
    private final static String PROPERTYFILENAME = "migration";

    private LiquibaseUpdate() {
    }

    public static Builder newBuilder() {
        return new LiquibaseUpdate().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setUrl(String url) {
            LiquibaseUpdate.this.url = url;
            return this;
        }

        public Builder setUser(String user) {
            LiquibaseUpdate.this.user = user;
            return this;
        }

        public Builder setPassword(String password) {
            LiquibaseUpdate.this.password = password;
            return this;
        }

        public LiquibaseUpdate build() {
            return LiquibaseUpdate.this;
        }
    }

    public void runLiquibase() throws LiquibaseException, SQLException {
        String path = getFromProperties(PROPERTYFILENAME, PATHNAME);
        Connection connection = DriverManager.getConnection(url, user, password);
        DriverManager.registerDriver(new Driver());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        CommandLineResourceAccessor commandLineResourceAccessor = new CommandLineResourceAccessor(classLoader);
        CompositeResourceAccessor compositeResourceAccessor = new CompositeResourceAccessor(commandLineResourceAccessor);
        Liquibase liquibase = new Liquibase(path, compositeResourceAccessor, new JdbcConnection(connection));
        liquibase.update(new Contexts());
    }
    //    Get value from migration.properties
    public String getFromProperties(String propertyFileName, String propertyKey) {
        ResourceBundle rb = ResourceBundle.getBundle(propertyFileName);
        return rb.keySet().stream().filter(key -> key.equals(propertyKey))
                .map(key -> rb.getString(key))
                .findFirst().map(Object::toString).orElse("");
    }
}
