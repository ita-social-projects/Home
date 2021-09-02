package com.softserveinc.ita.homeproject.api.tests.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.QueryFilterUtils;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;

class QueryUserIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

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
        ReadUser user = createBaseUserForTests();

        List<ReadUser> actualListUsers = new UserQuery
                .Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .email(user.getEmail())
                .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getEmail()).isEqualTo(user.getEmail()));
    }

    @Test
    void getAllUsersByFirstNameTest() throws ApiException {

        ReadUser user = createBaseUserForTests();

        List<ReadUser> actualListUsers = new UserQuery
                .Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .firstName(user.getFirstName())
                .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getFirstName()).isEqualTo(user.getFirstName()));
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
        ReadUser user = createBaseUserForTests();


        List<ReadUser> actualListUsers = new UserQuery
                .Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .lastName(user.getLastName())
                .build().perfom();

        actualListUsers.forEach(readUser -> assertThat(readUser.getLastName()).isEqualTo(user.getLastName()));
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
                .forEach(readUser -> assertThat(Objects.requireNonNull(readUser.getFirstName()).toLowerCase()).contains("AL".toLowerCase()));
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

    private List<ReadUser> saveListUser() {
        List<CreateUser> list = createUsersList();
        List<ReadUser> userList = new ArrayList<>();
        for (CreateUser cu : list) {
            userList.add(createBaseUserForTests());
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
    private CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .address(createAddress());
    }

    private CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("test.receive.apartment@gmail.com");
    }

    private Address createAddress() {
        return new Address().city("Dnepr")
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode");
    }

    private String getDecodedMessageByEmail(MailHogApiResponse response, String email) {
        String message="";
        for (int i=0; i<response.getItems().size(); i++){
            if(response.getItems().get(i).getContent().getHeaders().getTo().contains(email)){
                message = response.getItems().get(i).getMime().getParts().get(0).getMime().getParts().get(0).getBody();
                break;
            }
        }
        return new String(Base64.getMimeDecoder().decode(message), StandardCharsets.UTF_8);
    }

    private String getToken(String str) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(str);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }

        return result.trim();
    }

    @SneakyThrows
    private ReadUser createBaseUserForTests() {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,createCoop.getAdminEmail())));
        expectedUser.setEmail(createCoop.getAdminEmail());
        return userApi.createUser(expectedUser);
    }


    private void assertUser(CreateUser expected, ReadUser actual) {
        assertNotNull(expected);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

}
