import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String pass, user;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the username");
        user = scanner.nextLine();
        System.out.println("Please, enter the password");
        pass = scanner.nextLine();
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.runLiquibase(user, pass);
    }
}


