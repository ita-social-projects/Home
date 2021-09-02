package com.softserveinc.ita.homeproject.api.tests.security;

import com.softserveinc.ita.homeproject.client.api.ContactApi;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TempIT {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    @Test
    void someFind() {
        ContactApi apartmentApi = new ContactApi();
        Class<? extends ContactApi> someClass = apartmentApi.getClass();
        Method[] methods = someClass.getMethods();
//        System.out.println(Arrays.toString(someClass.getMethods()));
        Arrays.stream(methods)
                .filter((s) -> s.toString().contains("WithHttpInfo"))
                .map((s) -> s.toString().replaceAll(
                        "\\Qpublic com.softserveinc.ita.homeproject.ApiResponse com.softserveinc.ita.homeproject.api.ContactApi.\\E", ""))
                .map((s) -> s.split("\\)")[0] + ")")
//                .map((s) -> s.substring(0,s.compareTo(")")))
                .forEach(System.out::println);
    }

    /**
     * тест на получение всех классов из пакета
     */
    @Test
    void findAllClasses() {
        ContactApi apartmentApi = new ContactApi();

        Package aPackage = apartmentApi.getClass().getPackage();
        System.out.println(aPackage.toString()); // package com.softserveinc.ita.homeproject.api

        String packageName = apartmentApi.getClass().getPackageName();
        System.out.println(packageName); // com.softserveinc.ita.homeproject.api
        System.out.println("===================================");
//        List<Class<?>> find = find("home-application/src/main/java/com/softserveinc/ita/homeproject/application/api");
//        List<Class<?>> find = find("com.softserveinc.ita.homeproject com.softserveinc.ita.homeproject.application.api");
//        List<Class<?>> find = find("home-clients.target/generated-sources/openapi/client/src/gen/java/main/com/softserveinc/ita/homeproject/api");
//        List<Class<?>> find = find("C:\\Users\\vfrolo\\IdeaProjects\\Home\\home-clients\\target\\generated-sources\\openapi\\client\\src\\gen\\java\\main\\com\\softserveinc\\ita\\homeproject\\api");
//        List<Class<?>> find = find("com.softserveinc");
//        List<Class<?>> find = find(packageName);
//        List<Class<?>> find = find("com.softserveinc.ita.homeproject.application.api");
        List<Class<?>> find = find(packageName);
        int i = 0;
        for (Class<?> aClass : find) {
            System.out.println(++i + " --> " + aClass);
        }
    }


    /**
     * Возвращает список классов в пакете
     */
    public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        File[] listFiles = scannedDir.listFiles();
        for (File file : Objects.requireNonNull(listFiles)) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException | IncompatibleClassChangeError | ExceptionInInitializerError ignore) {
            }
        }
        return classes;
    }

    @Test
    void getAllClasses() {
        List<Class<?>> listOfClasses = getAllClassesFrom("com.softserveinc.ita.homeproject.application.client.api");
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

/*    @Test
    void getUrl() {
        String scannedUrl = Thread.currentThread().getContextClassLoader();
        System.out.println(scannedUrl);
    }*/

}