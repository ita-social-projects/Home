package com.softserveinc.ita.homeproject.homedatamigration.migrations;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@PropertySource("classpath:config/liquibase.properties")
public class Liquibase implements ApplicationRunner {

    @Value("${url}")
    private String url;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${changeLogFile}")
    private String changeLogFile;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Liquibase-update \n version = 0.0.1 \n " +
                "This application updates DB by using liquibase scripts. \" +\n" +
                "                \"Use full URL or combination of URL, user and password.");

        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog(changeLogFile);
        springLiquibase.setDataSource(getDataSource());
    }

    private DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}
