package com.softserveinc.ita.homeproject.api.tests.users;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
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
import org.junit.jupiter.api.Test;

class UserApiIT {


    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    @Test
    void createUserTest() throws ApiException {
        CreateUser expectedUser = createTestUser()
            .email("createUserTestExpcectedEmail@example.com");

        ApiResponse<ReadUser> response = userApi.createUserWithHttpInfo(expectedUser);

        assertEquals(Response.Status.CREATED.getStatusCode(),
            response.getStatusCode());
        assertUser(expectedUser, response.getData());
    }

    @Test
    void getUserTest() throws ApiException {
        CreateUser expectedUser = createTestUser()
            .email("getUserTestExpcectedEmail@example.com");

        ApiResponse<ReadUser> response = userApi.getUserWithHttpInfo(userApi.createUser(expectedUser).getId());

        assertEquals(Response.Status.OK.getStatusCode(),
            response.getStatusCode());
        assertUser(expectedUser, response.getData());
    }

    @Test
    void updateUserTest() throws ApiException {
        ReadUser savedUser = userApi
            .createUser(createTestUser().email("updateUserTestExpcectedEmail@example.com"));
        UpdateUser updateUser = new UpdateUser()
            .firstName("updatedFirstName")
            .lastName("updatedLastName");

        ApiResponse<ReadUser> response = userApi.updateUserWithHttpInfo(savedUser.getId(), updateUser);

        assertEquals(Response.Status.OK.getStatusCode(),
            response.getStatusCode());
        assertUser(savedUser, updateUser, response.getData());
    }

    @Test
    void deleteUserTest() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser()
            .email("deleteUserTestExpcectedEmail@example.com"));
        userApi.createUser(createTestUser()
            .email("secondDeleteUserTestExpcectedEmail@example.com"));
        userApi.createUser(createTestUser()
            .email("thirdDeleteUserTestExpcectedEmail@example.com"));

        ApiResponse<Void> removeResponse = userApi.removeUserWithHttpInfo(expectedUser.getId());

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
            .contacts(createContactList());
    }

    @Test
    void createUserInvalidEmailTest() {
        CreateUser createUserInvalidEmail = new CreateUser()
                .firstName("alan")
                .lastName("walker")
                .password("somePassword")
                .email("email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUserInvalidEmail))
                .withMessage("{\"responseCode\":400,\"errorMessage\":\"Parameter `email` is invalid - must meet"
                    + " the rule." + "\"}");
    }

    @Test
    void createUserInvalidPasswordTest() {
        CreateUser createUserInvalidPassword = new CreateUser()
            .firstName("alan")
            .lastName("walker")
            .password("some password")
            .email("email@example.com");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> userApi.createUser(createUserInvalidPassword))
            .withMessage("{\"responseCode\":400,\"errorMessage\":\"Parameter `password` is invalid - must meet"
                + " the rule." + "\"}");
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
