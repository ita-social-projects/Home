package com.softserveinc.ita.homeproject.application;

import java.util.Properties;
import javax.sql.DataSource;

import com.softserveinc.ita.homeproject.application.impl.HomeUserDetailsService;
import com.softserveinc.ita.homeproject.application.security.filter.JWTProvider;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan(basePackages = {"com.softserveinc.ita.homeproject.homeservice",
    "com.softserveinc.ita.homeproject.homedata"})
@EnableJpaRepositories("com.softserveinc.ita.homeproject.homedata")
@PropertySource("classpath:application-home-application-test.properties")
public class HomeServiceTestContextConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.url(env.getProperty("spring.datasource.url"));
        dataSourceBuilder.username(env.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(env.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em
            = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan("com.softserveinc.ita.homeproject.homeservice",
            "com.softserveinc.ita.homeproject.homedata");
        em.setJpaProperties(addProperties());
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    private Properties addProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        return properties;
    }

    @Bean
    public JavaMailSender mailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JWTProvider providerGenerator(UserSessionService userSessionService, UserDetailsService userDetailsService) {
        return new JWTProvider(userSessionService, userDetailsService);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository,
                                                 UserCooperationRepository userCooperationRepository) {
        return new HomeUserDetailsService(userRepository, userCooperationRepository);
    }
}
