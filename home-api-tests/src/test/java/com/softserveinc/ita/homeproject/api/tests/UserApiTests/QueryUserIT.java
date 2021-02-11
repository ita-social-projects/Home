package com.softserveinc.ita.homeproject.api.tests.UserApiTests;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtil;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class QueryUserIT {

    private UserApi userApi;
    private Integer pageNumber = 1;
    private Integer pageSize = 10;


    private final CreateUser expectedUser = new CreateUser()
            .firstName("Bill")
            .lastName("White")
            .password("password");

    @BeforeEach
    public void setUp() {
        userApi = new UserApi(ApiClientUtil.getClient());
        expectedUser.setEmail(RandomStringUtils.randomAlphabetic(5).concat("@example.com"));
    }

    @Test
    void getAllUsersByEmailTest() throws ApiException {
        userApi.createUser(expectedUser);

        List<ReadUser> actualListUsers = new UserQuery.Builder(pageNumber, pageSize, userApi).
                email(expectedUser.getEmail()).build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getEmail()).isEqualTo(expectedUser.getEmail()));
    }

    @Test
    void getAllUsersByFirstNameTest() throws ApiException {
        userApi.createUser(expectedUser);

        List<ReadUser> actualListUsers = new UserQuery.Builder(pageNumber, pageSize, userApi)
                .firstName(expectedUser.getFirstName())
                .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getFirstName()).isEqualTo(expectedUser.getFirstName()));
    }

    @Test
    void getAllUsersAscSortByLastNameTest() throws ApiException {
        saveListUser();
        String sort = "lastName,asc";

        List<ReadUser> actualListUsers = new UserQuery.Builder(pageNumber, pageSize, userApi)
                .sort(sort)
                .build().perfom();

        assertThat(actualListUsers).isNotEmpty().isSortedAccordingTo((r1, r2) -> {
            if (r1.getFirstName() == null) {
                return -1;
            }
            if (r2.getFirstName() == null) {
                return 1;
            }
            return r1.getFirstName().compareTo(r2.getFirstName());
        });
    }

    @Test
    void getAllUsersDescSortByFirstNameTest() throws ApiException {
        saveListUser();
        String sort = "firstName,desc";

        List<ReadUser> actualListUsers = new UserQuery.Builder(pageNumber, pageSize, userApi)
                .sort(sort)
                .build().perfom();

        assertThat(actualListUsers).isNotEmpty().isSortedAccordingTo((r1, r2) -> {
            if (r2.getFirstName() == null) {
                return -1;
            }
            if (r1.getFirstName() == null) {
                return 1;
            }
            return r2.getFirstName().compareTo(r1.getFirstName());
        });
    }

    @Test
    void getAllUsersByLastNameTest() throws ApiException {
        userApi.createUser(expectedUser);

        List<ReadUser> actualListUsers = new UserQuery.Builder(pageNumber, pageSize, userApi)
                .lastName(expectedUser.getLastName())
                .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getLastName()).isEqualTo(expectedUser.getLastName()));
    }


    @Test
    void getAllUsersLikeIgnoreCaseTest() throws ApiException {
        String filter = QueryFilterUtil.ignoreCaseLike("firstName", StringEscapeUtils.escapeJava("'AL'"));

        List<ReadUser> actualListUsers = new UserQuery.Builder(pageNumber, pageSize, userApi)
                .filter(filter)
                .build()
                .perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getFirstName().toLowerCase()).contains("AL".toLowerCase()));
    }

    @Test
    void wrongArgumentTest() {
        String firstName = " ";
        ApiException exception =
                new ApiException(400, "Illegal argument in select query method in api implementation");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> new UserQuery.Builder(pageNumber, pageSize, userApi)
                        .firstName(firstName)
                        .build()
                        .perfom())
                .withMessage(new StringBuilder()
                        .append("{\"responseCode\":")
                        .append(exception.getCode())
                        .append(",\"errorMessage\":\"")
                        .append(exception.getMessage())
                        .append("\"}").toString());
    }

    private void saveListUser() throws ApiException {
        List<CreateUser> list = createUsersList();
        for (CreateUser cu : list) {
            userApi.createUser(cu);
        }
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
}
