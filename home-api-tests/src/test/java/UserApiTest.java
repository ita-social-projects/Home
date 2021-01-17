import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ServerConfiguration;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiTest {

    private final UserApi userApi;

    {
        ApiClient client = new ApiClient();
        client.setUsername("admin@example.com");
        client.setPassword("password");
        client.setServers(List.of(new ServerConfiguration("http://localhost:8080/api/0",
                "No description provided", new HashMap())));
        userApi = new UserApi(client);
    }

    private final CreateUser createUser = new CreateUser()
            .firstName("firstName")
            .lastName("lastName")
            .password("password");

    @BeforeEach
    public void generateRandomEmail() {
        createUser.setEmail(RandomStringUtils.randomAlphabetic(5).concat("@example.com"));
    }

    @Test
    public void createUserTest() throws ApiException {
        ReadUser readUser = userApi.createUser(createUser);
        assertUser(createUser, readUser);
    }

    @Test
    public void getAllUsersTest() throws ApiException {
        userApi.createUser(createUser);
        List<ReadUser> readUsers = userApi.queryUsers(1, 10);

        assertNotNull(readUsers);
        assertFalse(readUsers.isEmpty());
    }

    @Test
    public void getUserByIdTest() throws ApiException {
        ReadUser savedUsers = userApi.createUser(createUser);
        ReadUser user = userApi.getUser(savedUsers.getId());

        assertNotNull(user);
        assertEquals(user, savedUsers);
    }

    @Test
    public void updateUserTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createUser);
        UpdateUser updateUser = new UpdateUser()
                .firstName("updatedFirstName")
                .lastName("updatedLastName");

        ReadUser updatedUser = userApi.updateUser(savedUser.getId(), updateUser);
        assertUser(savedUser, updateUser, updatedUser);
    }

    @Test
    @Disabled //TODO: add assert to check that user enabled flag is false
    public void deleteUserTest() throws ApiException {
        ReadUser savedUser = userApi.createUser(createUser);
        userApi.removeUser(savedUser.getId());

        ReadUser user = userApi.getUser(savedUser.getId());
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