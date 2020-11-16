// FIXME Basepackage here

public class Main {
    public static void main(String[] args) {
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.runLiquibase(
                "root", // FIXME should be taken from args
                "1234", // FIXME should be taken from args
                "jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true&serverTimezone=UTC", // FIXME should be taken from args
                "classpath:/db/changelog/db.changelog-master.xml" // FIXME should be taken from args
        );
    }
}
