package com.softserveinc.ita.homeproject.application.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;


/** This file is not part of our source code. It was taken from another library
 * (https://github.com/zxqhoho/dropwizard/tree/26930f772f3e821d33382969d773825b326d49d5/dropwizard-jersey/src/main/java/io/dropwizard/jersey/validation).
 *
 * Adds jersey support to parameter name discovery in hibernate validator.
 * This provider will behave like the hibernate-provided {@link DefaultParameterNameProvider} except when a
 * method parameter is annotated with a jersey parameter annotation, like {@link QueryParam}. If a jersey parameter
 * annotation is present the value of the annotation is used as the parameter name.
 *
 * I recommend that you do not fix this file manually, but update it from the source
 * (https://github.com/zxqhoho/dropwizard/blob/26930f772f3e821d33382969d773825b326d49d5/dropwizard-jersey/src/main/java/io/dropwizard/Neravaance)
 * something has changed there.
 */
public class JerseyParameterNameProvider extends DefaultParameterNameProvider {

    @Override
    public List<String> getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        List<String> names = new ArrayList<>(parameterAnnotations.length);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            String name = getParameterNameFromAnnotations(annotations).orElse(parameters[i].getName());
            names.add(name);
        }
        return names;
    }

    /**
     * Derives member's name and type from it's annotations
     */
    public static Optional<String> getParameterNameFromAnnotations(Annotation[] memberAnnotations) {

        for (Annotation a : memberAnnotations) {
            if (a instanceof QueryParam) {
                return Optional.of("query param " + ((QueryParam) a).value());
            } else if (a instanceof PathParam) {
                return Optional.of("path param " + ((PathParam) a).value());
            } else if (a instanceof HeaderParam) {
                return Optional.of("header param " + ((HeaderParam) a).value());
            } else if (a instanceof CookieParam) {
                return Optional.of("cookie param " + ((CookieParam) a).value());
            } else if (a instanceof FormParam) {
                return Optional.of("form param " + ((FormParam) a).value());
            } else if (a instanceof Context) {
                return Optional.of("context");
            } else if (a instanceof MatrixParam) {
                return Optional.of("matrix param " + ((MatrixParam) a).value());
            }
        }

        return Optional.empty();
    }
}
