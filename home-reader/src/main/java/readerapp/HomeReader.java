package readerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource({
//        "application-home-service.properties",
//        "application-home-data.properties"
//})
public class HomeReader {
    public static void main(String[] args) {
        SpringApplication.run(HomeReader.class, args);
    }
}
