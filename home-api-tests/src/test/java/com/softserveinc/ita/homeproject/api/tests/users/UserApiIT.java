package com.softserveinc.ita.homeproject.api.tests.users;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class UserApiIT {

    private final UserApi userApi = new UserApi(ApiClientUtil.getAdminClient());
    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getAdminClient());

    private final UserApi unauthorizedUserApi = new UserApi(ApiClientUtil.getUnauthorizedUserClient());

    @Test
    void getUserTest() throws ApiException {
        ReadUser readUser = createBaseUserForTests();

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
                .lastName("updatedLastName")
                .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
                .password("somePassword");

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
        ReadUser expectedUser = createBaseUserForTests();

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
    void createUserWithNonExistRegistrationToken() {
        CreateUser createUser = new CreateUser()
                .firstName("FirstName")
                .registrationToken(RandomStringUtils.randomAlphabetic(36))
                .lastName("LastNmae")
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
                .lastName("LastNmae")
                .password("somePassword")
                .email("walker@email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUser))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `registrationToken` is invalid - must not be null.");
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
    void updateUserWithEmptyFirstNameAndLastNameTest() {
        ReadUser savedUser = createBaseUserForTests();
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
    void updateUserWithInvalidFirstNameAndLastNameTest() {
        ReadUser savedUser = createBaseUserForTests();
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
    void updateUserWithEmptyEmailTest() {
        ReadUser savedUser = createBaseUserForTests();
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
    void updateUserWithInvalidEmailTest() {
        ReadUser savedUser = createBaseUserForTests();
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
    void updateUserWithEmptyPasswordTest() {
        ReadUser savedUser = createBaseUserForTests();
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
    void updateUserWithInvalidPasswordTest() {
        ReadUser savedUser = createBaseUserForTests();
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
    void updateUserWithNullParametersTest() {
        ReadUser savedUser = createBaseUserForTests();
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
                .registrationToken(RandomStringUtils.randomAlphabetic(36))
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
                .contacts(createContactList());
    }

    private CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .address(createAddress())
                .contacts(createContactList());
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

    private ApiResponse<ReadUser> createUserWithExistEmail() throws ApiException {
        ReadUser user = createBaseUserForTests();

        CreateUser userWithExistEmail = new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email(user.getEmail())
                .registrationToken(RandomStringUtils.randomAlphabetic(36))
                .contacts(createContactList());

        return userApi.createUserWithHttpInfo(userWithExistEmail);
    }

    private ApiResponse<ReadUser> updateUserWithExistEmail() throws ApiException {

        ReadUser savedUser = createBaseUserForTests();

        UpdateUser updateUser = new UpdateUser()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email(createBaseUserForTests().getEmail())
                .password("somePassword");

        return userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);
    }

    @SneakyThrows
    private ReadUser createBaseUserForTests() {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse, createCoop.getAdminEmail())));
        expectedUser.setEmail(createCoop.getAdminEmail());
        return userApi.createUser(expectedUser);
    }

    private ReadUser createNotMatchingUser() throws ApiException, InterruptedException, IOException {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,createCoop.getAdminEmail())));
        return userApi.createUser(expectedUser);
    }

    private void assertUser(ReadUser expected, ReadUser actual) {
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
