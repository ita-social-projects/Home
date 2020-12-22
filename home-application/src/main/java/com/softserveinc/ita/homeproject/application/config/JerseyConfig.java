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

/**
 * JerseyConfig is used for scan and registration of classes.
 * It lets you manually register classes and instances of
 * resources and providers manually.
 *
 * Basically what happens is that the implementor of the initializer
 * can tell the servlet container what classes to look for, and the
 * servlet container will pass those classes to the initializer method.
 *
 * @see org.glassfish.jersey.server.ResourceConfig it extends Application
 *
 * @author Mykyta Morar
 * @author Ihor Svyrydenko
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
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        // register endpoints
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        this.registerClasses(scanner.findCandidateComponents("com.softserveinc.ita.homeproject.api").stream()
                .map(beanDefinition -> ClassUtils
                        .resolveClassName(beanDefinition.getBeanClassName(), this.getClassLoader()))
                .collect(Collectors.toSet()));
        // register jackson for json
        register(JacksonJsonProvider.class);
    }
}
