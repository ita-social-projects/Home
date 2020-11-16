import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseUpdate {
   public void runLiquibase(String user, String password) throws DatabaseException, SQLException {
       java.sql.Connection connection = null;
       final String url = "jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true&serverTimezone=UTC";
       Liquibase liquibase = null;
       try {
           DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver ());
           connection = DriverManager.getConnection(url, user, password);;
           Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
           liquibase = new Liquibase("db.changelog-master.xml", new FileSystemResourceAccessor(), database);
           liquibase.update("first_migration");
       } catch (SQLException | LiquibaseException e) {
           throw new DatabaseException(e);
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
}
