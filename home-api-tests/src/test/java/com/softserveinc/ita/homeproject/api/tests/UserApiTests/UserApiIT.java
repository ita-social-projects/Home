package com.softserveinc.ita.homeproject.api.tests.UserApiTests;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
