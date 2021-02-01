package com.softserveinc.ita.homeproject.application.config.rsql;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class RSQLConfig {

    @Autowired
    private Set<RSQLEndpointConfig<? extends BaseEntity, ? extends QueryParamEnum>> rsqlEndpointConfigSet;

    @PostConstruct
    public void init() {
        rsqlEndpointConfigSet.forEach(config -> {
            Class<? extends BaseEntity> entityClass = config.getQueryConfig().getEntityClass();
            for (Map.Entry<? extends QueryParamEnum, String> entry : config.getMappings().entrySet()) {
                RSQLJPASupport.addMapping(entityClass, entry.getKey().getParameter(), entry.getValue());
            }

            RSQLJPASupport.addPropertyWhitelist(
                    entityClass,
                    config.getQueryConfig().getWhiteListEnums().stream()
                            .map(QueryParamEnum::getParameter)
                            .collect(Collectors.toList())
            );
        });
    }
}
