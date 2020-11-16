import liquibase.exception.DatabaseException;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws DatabaseException, SQLException {
        String pass, user;
        Scanner scanner = new Scanner(System.in);
        user = scanner.nextLine();
        pass = scanner.nextLine();
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.runLiquibase(user, pass);
    }
}
