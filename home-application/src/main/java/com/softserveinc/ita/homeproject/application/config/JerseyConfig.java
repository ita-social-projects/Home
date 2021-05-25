package com.softserveinc.ita.homeproject.application.config;

import java.util.stream.Collectors;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * JerseyConfig is used for classes' scan and registration
 * It allows you manually register classes and instances of
 * resources and providers.
 * <p>
 * Basically what happens here. The implementor of the initializer
 * can tell the servlet container which classes to look for, and the
 * servlet container will pass those classes to the initializer method.
 *
 * @author Mykyta Morar
 * @author Ihor Svyrydenko
 * @see org.glassfish.jersey.server.ResourceConfig it extends Application
 */
@Component
@ApplicationPath("/api/0")
public class JerseyConfig extends ResourceConfig {

    /**
     * Constructor is used to register endpoints and Jackson
     */
    public JerseyConfig() {
        // register endpoints
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        property(ServletProperties.FILTER_STATIC_CONTENT_REGEX, ".*/apidocs/.*");
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        // register endpoints
        this.registerPackageClasses("com.softserveinc.ita.homeproject.application.api", scanner);
        //  register exception mappers
        this.registerPackageClasses("com.softserveinc.ita.homeproject.application.exception.mapper", scanner);
        // register providers
        this.registerPackageClasses("com.softserveinc.ita.homeproject.application.provider", scanner);
    }

    private void registerPackageClasses(String packageName, ClassPathScanningCandidateComponentProvider scanner) {
        registerClasses(scanner.findCandidateComponents(packageName).stream()
            .map(beanDefinition -> ClassUtils
                .resolveClassName(beanDefinition.getBeanClassName(), this.getClassLoader()))
            .collect(Collectors.toSet()));
    }
}
