package com.softserveinc.ita.homeproject.config;

import com.softserveinc.ita.homeproject.api.NewsApi;
import com.softserveinc.ita.homeproject.api.NewsApiService;
import com.softserveinc.ita.homeproject.api.impl.NewsApiServiceImpl;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        //register("com.softserveinc.ita.homeproject.api");
        register(NewsApi.class);
    }

    @Bean
    public NewsApiService newsApiService(){
        return new NewsApiServiceImpl();
    }
}
