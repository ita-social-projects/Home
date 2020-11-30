package com.softserveinc.ita.homeproject.application.config.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DbCustomHealthCheck implements HealthIndicator {

    private final JdbcTemplate template;

    @Override
    public Health health() {
        int errorCode = check(); // perform some specific health check
        if (errorCode != 1) {
            return Health.down().withDetail("Error Code", 500).build();
        }
        return Health.up().build();
    }

    public int check(){
        List<Object> results = template.query("SELECT 1",
                new SingleColumnRowMapper<>());
        return results.size();
    }
}
