package com.softserveinc.ita.homeproject.application.config.query.rsql;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.softserveinc.ita.homeproject.application.config.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.application.converter.StringToContactTypeConverter;
import com.softserveinc.ita.homeproject.application.converter.StringToPollStatusConverter;
import com.softserveinc.ita.homeproject.application.converter.StringToPollTypeConverter;
import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import io.github.perplexhub.rsql.RSQLCommonSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RSQLConfig {

    @Autowired
    private Set<RSQLEndpointConfig<? extends BaseEntity, ? extends QueryParamEnum>> rsqlEndpointConfigSet;

    @PostConstruct
    public void init() {
        rsqlEndpointConfigSet.forEach(config -> {
            Class<? extends BaseEntity> entityClass = config.getQueryConfig().getEntityClass();
            for (Map.Entry<? extends QueryParamEnum, String> entry : config.getMappings().entrySet()) {
                RSQLCommonSupport.addMapping(entityClass, entry.getValue(), entry.getKey().getParameter());
            }

            RSQLCommonSupport.addConverter(new StringToContactTypeConverter());
            RSQLCommonSupport.addConverter(new StringToPollTypeConverter());
            RSQLCommonSupport.addConverter(new StringToPollStatusConverter());

            RSQLCommonSupport.addPropertyWhitelist(
                entityClass,
                config.getQueryConfig().getWhiteListEnums().stream()
                    .map(QueryParamEnum::getParameter)
                    .collect(Collectors.toList())
            );
        });
    }
}
