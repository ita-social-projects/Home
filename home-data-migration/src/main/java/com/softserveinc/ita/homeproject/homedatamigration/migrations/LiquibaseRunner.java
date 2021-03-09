package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name = "Liquibase-update",
    version = "0.0.1",
    mixinStandardHelpOptions = true,
    description = "This application updates DB by using liquibase scripts. "
        + "Use full URL or combination of URL, user and password.")
public class LiquibaseRunner implements Callable<Integer> {

    @Option(names = {
        "--url"}, description = "set url for your DB connection. It is required option.",
        defaultValue = "${DATASOURCE_URL}", required = true)
    private String url;

    @Option(names = {"-u",
        "--user"}, description = "set username for your DB connection", defaultValue = "${DATASOURCE_USER}")
    private String user;

    @Option(names = {"-p",
        "--password"}, description = "set password for your DB connection", defaultValue = "${DATASOURCE_PASSWORD}")
    private String password;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new LiquibaseRunner()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        boolean isCredentialsProvided = user != null && password != null;
        Connection connection = isCredentialsProvided ? getConnection(url, user, password) : getConnection(url);
        new LiquibaseUpdate(connection).runLiquibase();
        return 0;
    }

    public Connection getConnection(String fullUrl) throws SQLException {
        return DriverManager.getConnection(fullUrl);
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
