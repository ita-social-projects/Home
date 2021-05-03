package com.softserveinc.ita.homeproject.api.tests.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtils;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;

class QueryUserIT {

    private final CreateUser expectedUser = new CreateUser()
        .firstName("Bill")
        .lastName("White")
        .password("password");

    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    @Test
    void getAllUsers() throws ApiException {
        List<ReadUser> expectedListUser = saveListUser();

        List<ReadUser> actualListUsers = new UserQuery.Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .build()
            .perfom();

        assertThat(actualListUsers).isNotEmpty();
    }

    @Test
    void getAllUsersByEmailTest() throws ApiException {
        expectedUser.setEmail(RandomStringUtils.randomAlphabetic(5).concat("@example.com"));
        userApi.createUser(expectedUser);

        List<ReadUser> actualListUsers = new UserQuery
            .Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .email(expectedUser.getEmail())
            .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getEmail()).isEqualTo(expectedUser.getEmail()));
    }

    @Test
    void getAllUsersByFirstNameTest() throws ApiException {
        expectedUser.setEmail(RandomStringUtils.randomAlphabetic(5).concat("@example.com"));
        userApi.createUser(expectedUser);

        List<ReadUser> actualListUsers = new UserQuery
            .Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .firstName("Bill")
            .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getFirstName()).isEqualTo("Bill"));
    }

    @Test
    void getAllUsersAscSortByLastNameTest() throws ApiException {
        saveListUser();

        List<ReadUser> actualListUsers = new UserQuery
            .Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("lastName,asc")
            .build()
            .perfom();

        assertThat(actualListUsers).isSortedAccordingTo((u1, u2) -> Objects
            .requireNonNull(u1.getLastName()).compareToIgnoreCase(Objects.requireNonNull(u2.getLastName())));
    }

    @Test
    void getAllUsersDescSortByFirstNameTest() throws ApiException {
        saveListUser();

        List<ReadUser> actualListUsers = new UserQuery
            .Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("firstName,desc")
            .build().perfom();

        assertThat(actualListUsers).isSortedAccordingTo((u1, u2) -> Objects
            .requireNonNull(u2.getFirstName()).compareToIgnoreCase(Objects.requireNonNull(u1.getFirstName())));
    }

    @Test
    void getAllUsersByLastNameTest() throws ApiException {
        expectedUser.setEmail(RandomStringUtils.randomAlphabetic(5).concat("@example.com"));
        userApi.createUser(expectedUser);

        List<ReadUser> actualListUsers = new UserQuery
            .Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .lastName("White")
            .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getLastName()).isEqualTo("White"));
    }


    @Test
    void getAllUsersLikeIgnoreCaseTest() throws ApiException {
        saveListUser();
        String filter = QueryFilterUtils.getIgnoreCaseLikePredicate("firstName", StringEscapeUtils.escapeJava("'AL'"));

        List<ReadUser> actualListUsers = new UserQuery
            .Builder(userApi)
            .pageNumber(1)
            .pageSize(10)
            .filter(filter)
            .build()
            .perfom();

        actualListUsers
            .forEach(readUser -> assertThat(readUser.getFirstName().toLowerCase()).contains("AL".toLowerCase()));
    }

    @Test
    void emptyArgumentExceptionTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new UserQuery.Builder(userApi)
                .firstName(" ")
                .pageNumber(1)
                .pageSize(10)
                .build()
                .perfom())
            .matches(exception -> exception.getCode() == 400)
            .withMessageContaining("The query argument for search is empty");
    }

    @Test
    void wrongFilterFieldExceptionTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new UserQuery.Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .filter("firstNam=like='Al'")
                .build()
                .perfom())
            .matches(exception -> exception.getCode() == 400)
            .withMessageContaining("Unknown property: firstNam from entity");
    }

    @Test
    void wrongFilterPredicateExceptionTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new UserQuery.Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .filter("firstName=lik='Al'")
                .build()
                .perfom())
            .matches(exception -> exception.getCode() == 400)
            .withMessageContaining("Unknown operator: =lik=");
    }

    private List<ReadUser> saveListUser() throws ApiException {
        List<CreateUser> list = createUsersList();
        List<ReadUser> userList = new ArrayList<>();
        for (CreateUser cu : list) {
            userList.add(userApi.createUser(cu));
        }
        return userList;
    }

    private List<CreateUser> createUsersList() {
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
        return list;
    }

    private void assertUser(CreateUser expected, ReadUser actual) {
        assertNotNull(expected);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

}
