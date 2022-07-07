package com.softserveinc.ita.homeproject.api.tests.users;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.MailUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import com.softserveinc.ita.homeproject.client.model.UpdateUser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UserApiIT {

    private static final UserApi userApi = new UserApi(ApiClientUtil.getCooperationAdminClient());

    private static final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final UserApi unauthorizedUserApi = new UserApi(ApiClientUtil.getUserGuestClient());

    private static ReadUser baseUserForTests;

    @BeforeAll
    static void setUp() {
        baseUserForTests = createBaseUserForTests();
    }

    @Test
    void getUserTest() throws ApiException {
        ReadUser readUser = baseUserForTests;

        ApiResponse<ReadUser> response = userApi.getUserWithHttpInfo(readUser.getId());

        assertEquals(Response.Status.OK.getStatusCode(),
                response.getStatusCode());
        assertUser(readUser, response.getData());
    }

    @Test
    void updateUserTest() throws ApiException {
        ReadUser savedUser = createBaseUserForTests();
        UpdateUser updateUser = new UpdateUser()
                .firstName("updatedFirstName")
                .middleName("updatedMiddleName")
                .lastName("updatedLastName");

        ApiResponse<ReadUser> response = userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);

        assertEquals(Response.Status.OK.getStatusCode(),
                response.getStatusCode());
        assertUser(savedUser, updateUser, response.getData());
    }

    @Test
    void createUserWithNotMatchingEmail() {

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(this::createNotMatchingUser)
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("The e-mail to which the token was sent")
                .withMessageContaining("does not match provided");
    }

    @Test
    void deleteUserTest() throws ApiException {
        ReadUser expectedUser = baseUserForTests;

        ApiResponse<Void> removeResponse = userApi.deleteUserWithHttpInfo(expectedUser.getId());

        List<ReadUser> actualUsersList = new UserQuery
                .Builder(userApi)
                .pageNumber(1)
                .pageSize(10)
                .build().perfom();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removeResponse.getStatusCode());
        assertFalse(actualUsersList.contains(expectedUser));
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.getUser(expectedUser.getId()));
    }

    @Test
    void unauthorizedRequestTest() {
        CreateUser expectedUser = createTestUser();
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> unauthorizedUserApi.createUserWithHttpInfo(expectedUser))
                .matches(exception -> exception.getCode() == Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void createUserWithAlreadyExistEmail() {
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(this::createUserWithExistEmail)
                .withMessageContaining("User with email ").withMessageContaining(" is already exists");
    }

    @Test
    void createUserWithNonExistRegistrationToken() {
        CreateUser createUser = new CreateUser()
                .firstName("FirstName")
                .registrationToken(RandomStringUtils.randomAlphabetic(36))
                .middleName("MiddleName")
                .lastName("LastName")
                .password("somePassword")
                .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Invitation with provided token not found");
    }

    @Test
    void createUserWithRegistrationTokenNull() {
        CreateUser createUser = new CreateUser()
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .password("somePassword")
                .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `registrationToken` is invalid - must not be null.");
    }

    @Test
    void createUserWithEmptyFirstNameAndMiddleNameAndLastNameTest() {
        CreateUser createUser = new CreateUser()
                .firstName("")
                .middleName("")
                .lastName("")
                .password("somePassword")
                .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `middleName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createUserWithInvalidFirstNameAndMiddleNameAndLastNameTest() {
        CreateUser createUser = new CreateUser()
                .firstName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
                .middleName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
                .lastName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
                .password("somePassword")
                .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `middleName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createUserWithEmptyEmailTest() {
        CreateUser createUser = new CreateUser()
                .firstName("alan")
                .middleName("alanovich")
                .lastName("walker")
                .password("somePassword")
                .email("");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void createUserInvalidEmailTest() {
        CreateUser createUser = new CreateUser()
                .firstName("alan")
                .middleName("alanovich")
                .lastName("walker")
                .password("somePassword")
                .email("email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void createUserWithEmptyPasswordTest() {
        CreateUser createUser = new CreateUser()
                .firstName("alan")
                .middleName("alanovich")
                .lastName("walker")
                .password("")
                .email("email@example.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `password` is invalid - must meet the rule.");
    }

    @Test
    void createUserInvalidPasswordTest() {
        CreateUser createUser = new CreateUser()
                .firstName("alan")
                .middleName("alanovich")
                .lastName("walker")
                .password("some password")
                .email("email@example.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `password` is invalid - must meet the rule.");
    }

    @Test
    void createUserWithAllNullParametersTest() {
        CreateUser createUser = new CreateUser()
                .firstName(null)
                .middleName(null)
                .lastName(null)
                .email(null)
                .password(null);

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `firstName` is invalid - must not be null.")
                .withMessageContaining("Parameter `middleName` is invalid - must not be null.")
                .withMessageContaining("Parameter `lastName` is invalid - must not be null.")
                .withMessageContaining("Parameter `email` is invalid - must not be null.")
                .withMessageContaining("Parameter `password` is invalid - must not be null.");
    }

    @Disabled("error similar to Issue#250. Will be fixed in issue # 290.")
    @Test
    void createNullUserTest() {
        CreateUser expectedUser = null;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(expectedUser))
                .withMessageContaining("Missing the required parameter 'createUser' when calling createUser");
    }

    @Test
    void getNonExistentUserTest() {
        Long userId = Long.valueOf(RandomStringUtils.randomNumeric(10));

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.getUserWithHttpInfo(userId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("User with 'id: " + userId + "' is not found");
    }

    @Disabled("NotValid test. Will be fixed in issue # 290.")
    @Test
    void passNullWhenReceivingTest() {
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.getUserWithHttpInfo(null))
                .withMessageContaining("Missing the required parameter 'id' when calling getUser");
    }


    @Test
    void updateUserWithEmptyFirstNameAndMiddleNameAndLastNameTest() {
        ReadUser savedUser = baseUserForTests;
        UpdateUser updateUser = new UpdateUser()
                .firstName("")
                .middleName("")
                .lastName("");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `middleName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void updateUserWithInvalidFirstNameAndMiddleNameAndLastNameTest() {
        ReadUser savedUser = baseUserForTests;
        UpdateUser updateUser = new UpdateUser()
                .firstName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
                .middleName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
                .lastName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `middleName` is invalid - size must be between 1 and 50 signs.")
                .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }


    @Test
    void updateUserWithNullParametersTest() {
        ReadUser savedUser = baseUserForTests;
        UpdateUser updateUser = new UpdateUser()
                .firstName(null)
                .middleName(null)
                .lastName(null);

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `firstName` is invalid - must not be null.")
                .withMessageContaining("Parameter `lastName` is invalid - must not be null.");
    }

    @Test
    void deleteNonExistentUserTest() {
        Long userId = Long.valueOf(RandomStringUtils.randomNumeric(10));

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.deleteUserWithHttpInfo(userId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("User with id: " + userId + " is not found");
    }

    @Disabled("Associated with Issue# 250. Will be fixed in issue # 290.")
    @Test
    void passNullWhenDeleteUserTest() {
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.deleteUserWithHttpInfo(null))
                .withMessageContaining("Missing the required parameter 'id' when calling deleteUser");
    }

    private static List<CreateContact> createContactList() {
        List<CreateContact> createContactList = new ArrayList<>();
        createContactList.add(new CreateEmailContact()
                .email("primaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(true));

        createContactList.add(new CreateEmailContact()
                .email("secondaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(false));

        createContactList.add(new CreateEmailContact()
                .email("myemail@example.com")
                .type(ContactType.EMAIL)
                .main(false));

        createContactList.add(new CreateEmailContact()
                .email("youremail@example.com")
                .type(ContactType.EMAIL)
                .main(false));

        createContactList.add(new CreatePhoneContact()
                .phone("+380509999999")
                .type(ContactType.PHONE)
                .main(true));

        createContactList.add(new CreatePhoneContact()
                .phone("+380679999999")
                .type(ContactType.PHONE)
                .main(false));

        createContactList.add(new CreatePhoneContact()
                .phone("+380639999999")
                .type(ContactType.PHONE)
                .main(false));

        return createContactList;
    }

    private CreateUser createTestUser() {
        return new CreateUser()
                .registrationToken(RandomStringUtils.randomAlphabetic(36))
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .password("password")
                .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
                .contacts(createContactList());
    }

    private static CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomNumeric(8))
                .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
                .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .address(createAddress())
                .contacts(createContactList());
    }

    private static CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .password("password")
                .email("test.receive.apartment@gmail.com");
    }

    private static Address createAddress() {
        return new Address().city("Dnepr")
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode");
    }

    private ApiResponse<ReadUser> createUserWithExistEmail() throws ApiException {
        ReadUser user = baseUserForTests;

        CreateUser userWithExistEmail = new CreateUser()
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .password("password")
                .email(user.getEmail())
                .registrationToken(RandomStringUtils.randomAlphabetic(36))
                .contacts(createContactList());

        return userApi.createUserWithHttpInfo(userWithExistEmail);
    }

    private ApiResponse<ReadUser> updateUserWithExistEmail() throws ApiException {
        ReadUser savedUser = baseUserForTests;

        ReadUser additional = createBaseUserForTests();

        UpdateUser updateUser = new UpdateUser()
                .firstName("updatedFirstName")
                .middleName("middleName")
                .lastName("updatedLastName");

        return userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);
    }

    @SneakyThrows
    private static ReadUser createBaseUserForTests() {
        CreateCooperation coop = createBaseCooperation();
        CreateUser user = createBaseUser();
        return ApiClientUtil.createCooperationAdmin(cooperationApi, coop, userApi, user);
    }

    @SneakyThrows
    private ReadUser createNotMatchingUser()  {
        CreateCooperation coop = createBaseCooperation();
        cooperationApi.createCooperation(coop);
        String email = coop.getAdminEmail();

        ResponseEmailItem letter = MailUtil.waitLetterForEmail(email);

        CreateUser user = createBaseUser();
        user.setRegistrationToken(MailUtil.getToken(letter));
        return userApi.createUser(user);
    }

    private void assertUser(ReadUser expected, ReadUser actual) {
        assertNotNull(expected);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    private void assertUser(ReadUser saved, UpdateUser update, ReadUser updated) {
        assertNotNull(updated);
        assertNotEquals(saved, updated);
        assertEquals(update.getFirstName(), updated.getFirstName());
        assertEquals(update.getMiddleName(), updated.getMiddleName());
        assertEquals(update.getLastName(), updated.getLastName());
    }
}
