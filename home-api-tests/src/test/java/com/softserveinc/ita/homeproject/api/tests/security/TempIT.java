package com.softserveinc.ita.homeproject.api.tests.security;

import com.softserveinc.ita.homeproject.client.api.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class TempIT {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    ArrayList<String> allMethods = new ArrayList<>();

    @Test
    void someFind() {

        List<?> arrayListClasses = Arrays.asList(
                new ApartmentApi(),
                new ApartmentInvitationApi(),
                new ApartmentOwnershipApi(),
                new ContactApi(),
                new CooperationApi(),
                new CooperationContactApi(),
                new CooperationPollApi(),
                new HouseApi(),
                new InvitationsApi(),
                new NewsApi(),
                new PollApi(),
                new PolledHouseApi(),
                new PollQuestionApi(),
                new UserApi());

        ArrayList<String> arrayListMethods;
        for (Object arrayListClass : arrayListClasses) {
            arrayListMethods = getMethods(getArrayOfMethods(arrayListClass));
            allMethods.addAll(arrayListMethods);
        }

        int i = 0;
        for (String s : allMethods) {
            System.out.println(s);
        }
    }

    private Method[] getArrayOfMethods(Object someApi) {
        return someApi.getClass().getMethods();
    }

    private ArrayList<String> getMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter((s) -> s.toString().contains("WithHttpInfo"))
                .filter((s) -> s.toString().contains("approveInvitation"))
                .map((s) -> s.toString().replaceAll(
                        "\\Qpublic com.softserveinc.ita.homeproject.client.ApiResponse " +
                                "com.softserveinc.ita.homeproject.client.api.\\E", ""))
                .map((s) -> s.split("\\(")[0])
//                .map((s) -> s.split("\\.")[1])
//                .map((s) -> s.toString().replaceAll(
//                        "\\QWithHttpInfo\\E", ""))
                .map((s) -> s.split("\\(")[0])
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * тест на получение всех классов из пакета
     */
    @Test
    void findAllClasses() {
        ContactApi apartmentApi = null;

        try {
            Class<?> clazz = Class.forName(ContactApi.class.getName());
            apartmentApi = (ContactApi) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Package aPackage = apartmentApi != null ? apartmentApi.getClass().getPackage() : null;
        if (aPackage != null) {
            System.out.println(aPackage.toString()); // package com.softserveinc.ita.homeproject.api
        }

        String packageName = apartmentApi != null ? apartmentApi.getClass().getPackageName() : null;
        System.out.println(packageName); // com.softserveinc.ita.homeproject.api
        System.out.println("===================================");
//        List<Class<?>> find = find("home-application/src/main/java/com/softserveinc/ita/homeproject/application/api");
//        List<Class<?>> find = find("com.softserveinc.ita.homeproject com.softserveinc.ita.homeproject.application.api");
//        List<Class<?>> find = find("home-clients.target/generated-sources/openapi/client/src/gen/java/main/com/softserveinc/ita/homeproject/api");
//        List<Class<?>> find = find("C:\\Users\\vfrolo\\IdeaProjects\\Home\\home-clients\\target\\generated-sources\\openapi\\client\\src\\gen\\java\\main\\com\\softserveinc\\ita\\homeproject\\api");
//        List<Class<?>> find = find("com.softserveinc");
        List<Class<?>> find = find(Objects.requireNonNull(aPackage).toString());
//        List<Class<?>> find = find("com.softserveinc.ita.homeproject.api");
//        List<Class<?>> find = find(packageName);
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

    @SneakyThrows
    @Test
    List<Object> listWithInstanceClasses() {
        List<Class<?>> allClassesOfClientApi = getAllClassesOfClientApi();
        List<Object> classInstances = new ArrayList<>(allClassesOfClientApi.size());
        for (Class<?> aClass : allClassesOfClientApi) {
            classInstances.add(aClass.getConstructor().newInstance());
        }
        return classInstances;
    }

    @SneakyThrows
    List<Class<?>> getAllClassesOfClientApi() {
        return getAllClassesFrom(ContactApi.class.getPackageName());
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


    @Test
    void scanAllModules() {
        ModuleLayer.boot().configuration().modules().stream()
                .map(ResolvedModule::reference)
                .forEach(mref -> {
                    System.out.println(mref.descriptor().name());
                    try (ModuleReader reader = mref.open()) {
                        reader.list().forEach(System.out::println);
                    } catch (IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                });
    }

}