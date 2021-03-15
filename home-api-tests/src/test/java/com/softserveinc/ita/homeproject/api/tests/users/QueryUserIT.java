package com.softserveinc.ita.homeproject.api.tests.users;

import static com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtils.createExceptionMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;
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

        assertThat(actualListUsers).isNotEmpty().isSortedAccordingTo((r1, r2) -> {
            if (r1.getLastName() == null) {
                return -1;
            }
            if (r2.getLastName() == null) {
                return 1;
            }
            return r1.getLastName().compareTo(r2.getLastName());
        });
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
        ApiException exception =
            new ApiException(400, "The query argument for search is empty");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new UserQuery.Builder(userApi)
                .firstName(" ")
                .pageNumber(1)
                .pageSize(10)
                .build()
                .perfom())
            .withMessage(createExceptionMessage(exception));
    }

    @Test
    void wrongFilterFieldExceptionTest() {
        ApiException exception =
            new ApiException(400, "Unknown property: firstNam from entity");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new UserQuery.Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .filter("firstNam=like='Al'")
                .build()
                .perfom())
            .withMessage(createExceptionMessage(exception));
    }

    @Test
    void wrongFilterPredicateExceptionTest() {
        ApiException exception =
            new ApiException(400, "Unknown operator: =lik=");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> new UserQuery.Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .filter("firstName=lik='Al'")
                .build()
                .perfom())
            .withMessage(createExceptionMessage(exception));
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
