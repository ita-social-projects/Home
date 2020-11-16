import java.sql.Connection;
import java.sql.DriverManager;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.CommandLineResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

public class LiquibaseUpdate {
    public void runLiquibase(String user, String password, String url, String changeLogFile) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            FileSystemResourceAccessor fileSystemResourceAccessor = new FileSystemResourceAccessor();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            CommandLineResourceAccessor commandLineResourceAccessor = new CommandLineResourceAccessor(classLoader);
            CompositeResourceAccessor compositeResourceAccessor = new CompositeResourceAccessor(fileSystemResourceAccessor, commandLineResourceAccessor);
            try (Liquibase liquibase = new Liquibase(changeLogFile, compositeResourceAccessor, database)) {
                liquibase.update("first_migration");
            } catch (LiquibaseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
