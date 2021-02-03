package com.softserveinc.ita.homeproject.api.tests;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

public class RSQLUserIT {
    private List<ReadUser> userList = new ArrayList<>();
    private UserApi userApi;

    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    private String filter;
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String contact;

    @BeforeEach
    public void setUp() throws ApiException {
        userApi = new UserApi(ApiClientUtil.getClient());
        initParameters();
        if (!userList.isEmpty()) {
            userList.clear();
        }
        createListUserTest();
    }

    @Test
    void getAllUsersByEmailTest() throws ApiException {

        email = userList.get((int) (Math.random() * userList.size())).getEmail();
        List<ReadUser> readUsers = userApi
                .getAllUsers(pageNumber,
                        pageSize,
                        sort,
                        filter,
                        id,
                        email,
                        firstName,
                        lastName,
                        contact);

        assertFalse(readUsers.isEmpty());
        readUsers.forEach(readUser -> assertEquals(readUser.getEmail(), email));
    }

    @Test
    void getAllUsersByFirstNameTest() throws ApiException {
        firstName = userList.get((int) (Math.random() * userList.size())).getFirstName();

        List<ReadUser> readUsers = userApi
                .getAllUsers(pageNumber,
                        pageSize,
                        sort,
                        filter,
                        id,
                        email,
                        firstName,
                        lastName,
                        contact);

        assertFalse(readUsers.isEmpty());
        readUsers.forEach(readUser -> assertEquals(readUser.getFirstName(), firstName));
    }

    @Test
    void getAllUsersAscSortByLastNameTest() throws ApiException {
        sort = "lastName,asc";
        List<String> nameList = new ArrayList<>();

        List<ReadUser> readUsers = userApi
                .getAllUsers(pageNumber,
                        pageSize,
                        sort,
                        filter,
                        id,
                        email,
                        firstName,
                        lastName,
                        contact);
        readUsers.forEach(readUser -> nameList.add(readUser.getLastName()));
        String[] actualName = new String[nameList.size()];
        nameList.toArray(actualName);
        Collections.sort(nameList);
        String[] expectedName = new String[nameList.size()];
        nameList.toArray(expectedName);

        assertArrayEquals(expectedName, actualName);
    }

    @Test
    void getAllUsersDescSortByFirstNameTest() throws ApiException {
        sort = "firstName,desc";
        List<String> nameList = new ArrayList<>();

        List<ReadUser> readUsers = userApi
                .getAllUsers(pageNumber,
                        pageSize,
                        sort,
                        filter,
                        id,
                        email,
                        firstName,
                        lastName,
                        contact);
        readUsers.forEach(readUser -> nameList.add(readUser.getFirstName()));
        String[] actualName = new String[nameList.size()];
        nameList.toArray(actualName);
        Collections.sort(nameList, Collections.reverseOrder());
        String[] expectedName = new String[nameList.size()];
        nameList.toArray(expectedName);

        assertArrayEquals(expectedName, actualName);
    }

    @Test
    void getAllUsersByLastNameTest() throws ApiException {
        lastName = userList.get((int) (Math.random() * userList.size())).getLastName();

        List<ReadUser> readUsers = userApi
                .getAllUsers(pageNumber,
                        pageSize,
                        sort,
                        filter,
                        id,
                        email,
                        firstName,
                        lastName,
                        contact);

        assertFalse(readUsers.isEmpty());
        readUsers.forEach(readUser -> assertEquals(readUser.getLastName(), lastName));
    }

    @Test
    void getAllUsersLikeIgnoreCaseTest() throws ApiException {
        filter = "firstName=ilike='AL'";

        List<ReadUser> readUsers = userApi
                .getAllUsers(pageNumber,
                        pageSize,
                        sort,
                        filter,
                        id,
                        email,
                        firstName,
                        lastName,
                        contact);

        assertFalse(readUsers.isEmpty());
    }

    @Test
    void wrongArgumentTest() {
        firstName = " ";

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi
                        .getAllUsers(pageNumber,
                                pageSize,
                                sort,
                                filter,
                                id,
                                email,
                                firstName,
                                lastName,
                                contact))
                .withMessage(new StringBuilder()
                        .append("{").append("\"responseCode\"")
                        .append(":400,")
                        .append("\"errorMessage\"")
                        .append(":")
                        .append("\"Illegal argument for request.\"")
                        .append("}").toString());

    }

    @AfterEach
    void tearDown() throws ApiException {
        for (ReadUser ru : userList) {
            userApi.removeUser(ru.getId());

        }
    }

    private void createListUserTest() throws ApiException {
        List<CreateUser> list = createUsersList().orElseThrow();

        for (CreateUser cu : list) {
            userList.add(userApi.createUser(cu));
        }
        assertEquals(userList.size(), list.size());

        for (int i = 0; i < userList.size(); i++)
            assertUser(list.get(i), userList.get(i));
    }

    private void initParameters() {
        pageNumber = 1;
        pageSize = 10;
        sort = null;
        filter = null;
        id = null;
        email = null;
        firstName = null;
        lastName = null;
        contact = null;
    }

    private Optional<List<CreateUser>> createUsersList() {
        List<CreateUser> list = new ArrayList<>();
        list.add(new CreateUser().
                firstName("Alex").
                lastName("Young").
                email(RandomStringUtils.randomAlphabetic(5).concat("@example.com")).
                password("password")
        );
        list.add(new CreateUser().
                firstName("Bob").
                lastName("Smith").
                email(RandomStringUtils.randomAlphabetic(5).concat("@example.com")).
                password("password")
        );
        list.add(new CreateUser().
                firstName("Jack").
                lastName("Gray").
                email(RandomStringUtils.randomAlphabetic(5).concat("@example.com")).
                password("password")
        );
        list.add(new CreateUser().
                firstName("Sindy").
                lastName("Black").
                email(RandomStringUtils.randomAlphabetic(5).concat("@example.com")).
                password("password")
        );
        list.add(new CreateUser().
                firstName("Victor").
                lastName("Along").
                email(RandomStringUtils.randomAlphabetic(5).concat("@example.com")).
                password("password")
        );
        return Optional.of(list);
    }

    private void assertUser(CreateUser expected, ReadUser actual) {
        assertNotNull(expected);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }
}
