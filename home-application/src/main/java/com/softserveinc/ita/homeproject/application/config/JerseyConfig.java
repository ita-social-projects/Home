package com.softserveinc.ita.homeproject.application.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Component
@ApplicationPath("/api/0")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // register endpoints
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        // register endpoints
        this.registerPackageClasses("com.softserveinc.ita.homeproject.api", scanner);
        //  register exception mappers
        this.registerPackageClasses("com.softserveinc.ita.homeproject.application.exception.mapper", scanner);
        // register jackson for json
        register(JacksonJsonProvider.class);
    }

    private ResourceConfig registerPackageClasses(String packageName, ClassPathScanningCandidateComponentProvider scanner){
        registerClasses(scanner.findCandidateComponents(packageName).stream()
                .map(beanDefinition -> ClassUtils
                        .resolveClassName(beanDefinition.getBeanClassName(), this.getClassLoader()))
                .collect(Collectors.toSet()));
        return this;
    }
}
