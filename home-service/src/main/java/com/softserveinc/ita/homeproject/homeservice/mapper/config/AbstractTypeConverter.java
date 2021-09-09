package com.softserveinc.ita.homeproject.homeservice.mapper.config;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.BiPredicate;

import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class AbstractTypeConverter<S, D> implements ConditionalConverter<S, D> {

    private final String destinationPackagePrefix;

    private final String sourcePackagePrefix;

    private final TypeMatcher<S, D> typeMatcher;

    private final Reflections reflections;

    private final BiPredicate<Reflections, Class<?>> abstractChecker;

    public AbstractTypeConverter(String sourcePackagePrefix, String destinationPackagePrefix) {
        this(sourcePackagePrefix, destinationPackagePrefix, (sourceType, destinationType) -> {
            String destinationSimpleName = destinationType.getSimpleName();
            String sourceTypeSimpleName = sourceType.getSimpleName();
            return destinationSimpleName.contains(sourceTypeSimpleName)
                || sourceTypeSimpleName.contains(destinationSimpleName);
        });
    }

    public AbstractTypeConverter(String sourcePackagePrefix,
                                 String destinationPackagePrefix,
                                 TypeMatcher<S, D> typeMatcher) {
        this(sourcePackagePrefix, destinationPackagePrefix, typeMatcher,
            (r, destinationType) -> Modifier.isAbstract(destinationType.getModifiers()));
    }

    public AbstractTypeConverter(String sourcePackagePrefix,
                                 String destinationPackagePrefix,
                                 TypeMatcher<S, D> typeMatcher,
                                 BiPredicate<Reflections, Class<?>> abstractChecker) {
        this.destinationPackagePrefix = destinationPackagePrefix;
        this.sourcePackagePrefix = sourcePackagePrefix;
        this.typeMatcher = typeMatcher;
        this.abstractChecker = abstractChecker;
        this.reflections = new Reflections(this.destinationPackagePrefix, new SubTypesScanner());
    }

    @Override
    public D convert(MappingContext<S, D> context) {
        Set<Class<? extends D>> destinationSubTypes = reflections.getSubTypesOf(context.getDestinationType());
        Class<? extends S> sourceType = context.getSourceType();
        Class<? extends D> destinationSubType = destinationSubTypes.stream()
            .filter(destinationSybType -> this.typeMatcher.match(sourceType, destinationSybType))
            .findFirst()
            .orElseThrow();
        return context.getMappingEngine().map(context.create(context.getSource(), destinationSubType));
    }

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return
            destinationType.getPackageName().contains(destinationPackagePrefix)
                && sourceType.getPackageName().contains(sourcePackagePrefix)
                && abstractChecker.test(reflections, destinationType) ? MatchResult.FULL : MatchResult.NONE;
    }
}
