package com.softserveinc.ita.homeproject.api.tests.users;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserApiIT {
    private UserApi userApi;

    private final CreateUser createUser = new CreateUser()
            .firstName("firstName")
            .lastName("lastName")
            .password("password");

    @BeforeEach
    public void setUp() {
        userApi = new UserApi(ApiClientUtil.getClient());
        createUser.setEmail(RandomStringUtils.randomAlphabetic(5).concat("@example.com"));
    }

    @Test
    void createUserTest() throws ApiException {
        ReadUser readUser = userApi.createUser(createUser);

        assertUser(createUser, readUser);
    }

    @Test
    void getUserByIdTest() throws ApiException {
        ReadUser savedUsers = userApi.createUser(createUser);

        ReadUser user = userApi.getUser(savedUsers.getId());

        assertNotNull(user);
        assertEquals(user, savedUsers);
    }

    @Test
    void updateUserTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createUser);

        UpdateUser updateUser = new UpdateUser()
                .firstName("updatedFirstName")
                .lastName("updatedLastName");

        ReadUser updatedUser = userApi.updateUser(savedUser.getId(), updateUser);
        assertUser(savedUser, updateUser, updatedUser);
    }

    @Test
    void deleteUserTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createUser);
    }

    @Test
    void createUserInvalidParametersTest() {
        CreateUser createUserInvalidParameters = new CreateUser()
                .firstName("alan")
                .lastName("walker")
                .password("some p")
                .email("email.com");

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> userApi.createUser(createUserInvalidParameters))
                .withMessage("{\"responseCode\":400,\"errorMessage\":\"Parameter password is invalid - must match " +
                        "\"[a-zA-Z0-9]{8,}\". Parameter email is invalid - must match " +
                        "\\\"^[a-zA-Z0-9.!#$%&вЂ™*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$\\\"." + "\"}");
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
