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

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class UserApiIT {

    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());
    private final UserApi unauthorizedUserApi = new UserApi(ApiClientUtil.getUnauthorizedClient());

    @Test
    void createUserTest() throws ApiException {
        CreateUser expectedUser = createTestUser();

        ApiResponse<ReadUser> response = userApi.createUserWithHttpInfo(expectedUser);

        assertEquals(Response.Status.CREATED.getStatusCode(),
            response.getStatusCode());
        assertUser(expectedUser, response.getData());
    }

    @Test
    void getUserTest() throws ApiException {
        CreateUser expectedUser = createTestUser();

        ApiResponse<ReadUser> response = userApi.getUserWithHttpInfo(userApi.createUser(expectedUser).getId());

        assertEquals(Response.Status.OK.getStatusCode(),
            response.getStatusCode());
        assertUser(expectedUser, response.getData());
    }

    @Test
    void updateUserTest() throws ApiException {
        ReadUser savedUser = userApi
            .createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("updatedFirstName")
            .lastName("updatedLastName")
            .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
            .password("somePassword");

        ApiResponse<ReadUser> response = userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);

        assertEquals(Response.Status.OK.getStatusCode(),
            response.getStatusCode());
        assertUser(savedUser, updateUser, response.getData());
    }

    @Test
    void deleteUserTest() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());
        userApi.createUser(createTestUser());
        userApi.createUser(createTestUser());

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
            .matches(exception -> exception.getCode() == Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    void createUserWithAlreadyExistEmail() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(this::createUserWithExistEmail)
            .withMessageContaining("User with email ").withMessageContaining(" is already exists");
    }

    @Test
    void createUserWithEmptyFirstNameAndLastNameTest() {
        CreateUser createUser = new CreateUser()
            .firstName("")
            .lastName("")
            .password("somePassword")
            .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.createUser(createUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
            .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createUserWithInvalidFirstNameAndLastNameTest() {
        CreateUser createUser = new CreateUser()
            .firstName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
            .lastName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
            .password("somePassword")
            .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.createUser(createUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
            .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createUserWithEmptyEmailTest() {
        CreateUser createUser = new CreateUser()
            .firstName("alan")
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
            .lastName(null)
            .email(null)
            .password(null);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.createUser(createUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `firstName` is invalid - must not be null.")
            .withMessageContaining("Parameter `lastName` is invalid - must not be null.")
            .withMessageContaining("Parameter `email` is invalid - must not be null.")
            .withMessageContaining("Parameter `password` is invalid - must not be null.");
    }

    @Test
    void createNullUserTest() {
        CreateUser expectedUser = null;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.createUser(expectedUser))
            .withMessageContaining("Missing the required parameter 'createUser' when calling createUser");
    }

    @Test
    void getNonExistentUserTest() {
        Long userId = 100L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.getUserWithHttpInfo(userId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("User with 'id: " + userId + "' is not found");
    }

    @Test
    void passNullWhenReceivingTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.getUserWithHttpInfo(null))
            .withMessageContaining("Missing the required parameter 'id' when calling getUser");
    }

    @Test
    void updateUserWithAlreadyExistEmail() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(this::updateUserWithExistEmail)
            .withMessageContaining("User with email ").withMessageContaining(" is already exists");
    }

    @Test
    void updateUserWithEmptyFirstNameAndLastNameTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("")
            .lastName("")
            .email("mr.smith@gmail.com");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
            .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void updateUserWithInvalidFirstNameAndLastNameTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
            .lastName("AhmudIbnSalimDeAlpachinoStyleCreatedInAmericaStreet")
            .email("mr.smith@gmail.com");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `firstName` is invalid - size must be between 1 and 50 signs.")
            .withMessageContaining("Parameter `lastName` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void updateUserWithEmptyEmailTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("John")
            .lastName("Smith")
            .email("");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void updateUserWithInvalidEmailTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("John")
            .lastName("Smith")
            .email("mr.smith@");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void updateUserWithEmptyPasswordTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("John")
            .lastName("Smith")
            .email("mr.smith@")
            .password("");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `password` is invalid - must meet the rule.");
    }

    @Test
    void updateUserWithInvalidPasswordTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName("John")
            .lastName("Smith")
            .email("mr.smith@")
            .password("1234");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `password` is invalid - must meet the rule.");
    }

    @Test
    void updateUserWithNullParametersTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createTestUser());
        UpdateUser updateUser = new UpdateUser()
            .firstName(null)
            .lastName(null)
            .email(null);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `firstName` is invalid - must not be null.")
            .withMessageContaining("Parameter `lastName` is invalid - must not be null.")
            .withMessageContaining("Parameter `email` is invalid - must not be null.");
    }

    @Test
    void deleteNonExistentUserTest() {
        Long userId = 100L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.deleteUserWithHttpInfo(userId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("User with id: " + userId + " is not found");
    }

    @Test
    void passNullWhenDeleteUserTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.deleteUserWithHttpInfo(null))
            .withMessageContaining("Missing the required parameter 'id' when calling deleteUser");
    }

    private List<CreateContact> createContactList() {
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
            .firstName("firstName")
            .lastName("lastName")
            .password("password")
            .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
            .contacts(createContactList());
    }

    private ApiResponse<ReadUser> createUserWithExistEmail() throws ApiException {
        String email = RandomStringUtils.randomAlphabetic(5).concat("@example.com");

        userApi.createUser(new CreateUser()
            .firstName("firstName")
            .lastName("lastName")
            .password("password")
            .email(email)
            .contacts(createContactList()));

        CreateUser userWithExistEmail = new CreateUser()
            .firstName("firstName")
            .lastName("lastName")
            .password("password")
            .email(email)
            .contacts(createContactList());

        return userApi.createUserWithHttpInfo(userWithExistEmail);
    }

    private ApiResponse<ReadUser> updateUserWithExistEmail() throws ApiException {
        String email = RandomStringUtils.randomAlphabetic(5).concat("@example.com");

        ReadUser savedUser = userApi.createUser(new CreateUser()
            .firstName("firstName")
            .lastName("lastName")
            .password("password")
            .email((RandomStringUtils.randomAlphabetic(5).concat("@example.com")))
            .contacts(createContactList()));

        userApi.createUser(new CreateUser()
            .firstName("firstName")
            .lastName("lastName")
            .password("password")
            .email(email)
            .contacts(createContactList()));

        UpdateUser updateUser = new UpdateUser()
            .firstName("updatedFirstName")
            .lastName("updatedLastName")
            .email(email)
            .password("somePassword");

        return userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);
    }

    private void assertUser(CreateUser expected, ReadUser actual) {
        assertNotNull(expected);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    private void assertUser(ReadUser saved, UpdateUser update, ReadUser updated) {
        assertNotNull(updated);
        assertNotEquals(saved, updated);
        assertEquals(update.getFirstName(), updated.getFirstName());
        assertEquals(update.getLastName(), updated.getLastName());
    }
}
