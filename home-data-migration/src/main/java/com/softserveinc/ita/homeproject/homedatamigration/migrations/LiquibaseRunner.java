package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.exception.LiquibaseException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;


@Command (name = "Liquibase-update", description = "use full url or url, password and user")
public class LiquibaseRunner implements Callable<Integer> {

    @Option(names = {"-us", "--user"}, description = "set user")
    String user ;
    @Option(names = {"-p", "--password"}, description = "set password")
    String password;
    @Option(names = {"-u", "--url"}, description = "set url", required = true)
    String url;

    public static void main(String[] args) throws LiquibaseException, SQLException {
        int exitCode = new CommandLine(new LiquibaseRunner()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        // Full url to connecting to Db on Heroku: "jdbc:postgresql://ec2-34-254-24-116.eu-west-1.compute.amazonaws.com:5432/d1pi5jhrf0fbun?user=hwkbjsunlfuchw&password=c9039cc29d3150b2288c58d1eb988c7ea9a3a5b38eb117f39b5cb9bbf6fccd76";
        Connection connection = (user != null && password != null) ? getConnection(url, user, password) : getConnection(url);
        new LiquibaseUpdate().runLiquibase(connection);
        return 0;
    }

    public Connection getConnection(String fullUrl) throws SQLException {
        return DriverManager.getConnection(fullUrl);
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
