import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class LiquibaseUpdate {
//    name of references in migration.properties
    private static final String dbhost = "url";
    private static final String pathname = "changelog.path";
    private static final String changelog = "changelog.name";

    public void runLiquibase(String user, String password) {
//        Initializations
        String url = getFromProperties(dbhost);
        String path = getFromProperties(pathname);
        String changelogName = getFromProperties(changelog);
        Connection connection = null;
        Liquibase liquibase;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Database database = DatabaseFactory.getInstance().
                    findCorrectDatabaseImplementation(new JdbcConnection(connection));
            liquibase = new Liquibase(changelogName, new FileSystemResourceAccessor(new File(path)), database);
            liquibase.update(new Contexts());
        } catch (SQLException | LiquibaseException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }
    }
//    Get value from migration.properties
    public String getFromProperties(String value) {
        String url = "";
        try (InputStream input = new FileInputStream("home-data-migration\\src\\main\\resources\\migration.properties")) {
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


