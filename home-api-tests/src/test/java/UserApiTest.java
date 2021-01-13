import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserApiTest {
    private final ApiClient client = new ApiClient();
    private final UserApi userApi = new UserApi();

    {
        client.setUsername("admin@example.com");
        client.setPassword("password");
    }

    @Test
    public void getAllUsersTest () throws ApiException {
        List<ReadUser> readUsers = userApi.queryUsers(1, 10);
    }

    @Test
    public void addUserTest () throws ApiException {
        CreateUser user = new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .password("password");

        List<ReadUser> users = userApi.createUser(user);
    }

    @Test
    public void getUserTest () {

    }

}
