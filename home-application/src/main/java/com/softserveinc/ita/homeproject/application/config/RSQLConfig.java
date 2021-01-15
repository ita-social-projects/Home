package com.softserveinc.ita.homeproject.application.config;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.stereotype.Component;


@Component
public class RSQLConfig {

    public RSQLConfig() {
        configUserQuery();
    }

    private void configUserQuery() {
        RSQLJPASupport.addPropertyWhitelist(User.class, "firstName");
        RSQLJPASupport.addPropertyWhitelist(User.class, "lastName");
        RSQLJPASupport.addPropertyWhitelist(User.class, "email");
        RSQLJPASupport.addPropertyBlacklist(User.class, "password");
    }
}
