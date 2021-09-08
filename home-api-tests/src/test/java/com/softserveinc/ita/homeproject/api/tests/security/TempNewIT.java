package com.softserveinc.ita.homeproject.api.tests.security;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.client.api.ContactApi;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class TempNewIT {

    @SneakyThrows
    @Test
    void getAllMethodsFromInstance(){
        List<Class<?>> classList = getAllClasses();
/*        classList = classList.stream()
                .map((s) -> s.toString().split(" ")[1])
                .collect(Collectors.toList());*/
        System.out.println(classList); // --> [class com.softserveinc.ita.homeproje...

        String str = classList.get(0).toString().split(" ")[1];
        System.out.println(str); // com.softserveinc.ita.homeproject.client.api.HouseApi

        Class<?> c = Class.forName(str);
        System.out.println(c); // class com.softserveinc.ita.homeproject.client.api.HouseApi

        Constructor<?> cons = c.getConstructor();
        Object o = cons.newInstance();
        System.out.println(o); // com.softserveinc.ita.homeproject.client.api.HouseApi@1e636ea3

        Method[] methods = o.getClass().getMethods();
        System.out.println(Arrays.toString(methods)); // [public com.softserveinc.ita.homeproject.client.ApiClient com.softserveinc.ita.homeproject.client.api.HouseA...

        System.out.println("================");
        for (Method method : methods) {
            System.out.println(method);
        }

        System.out.println("-------------------------------");
        Class<?> c2 = Class.forName(str);
        Object o2 = c2.getConstructor().newInstance();
        System.out.println(o2);

        System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        Arrays.stream(methods)
                .filter(f -> f.toString().contains("WithHttpInfo"))
                .forEach(System.out::println);

        Parameter[] parameters = methods[2].getParameters();
        Arrays.stream(parameters).map(Parameter::toString).forEach(System.out::println);
    }
    List<Class<?>> getAllClasses() {
        ContactApi forLoadInClassLoader = new ContactApi();

        try {
            Class<?> classLoaderStart = Class.forName(forLoadInClassLoader.getClass().getName());
            forLoadInClassLoader = (ContactApi) classLoaderStart.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return getAllClassesFrom(Objects.requireNonNull(forLoadInClassLoader).getClass().getPackageName());
    }

    private List<Class<?>> getAllClassesFrom(String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
                .getAllTypes()
                .stream()
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
