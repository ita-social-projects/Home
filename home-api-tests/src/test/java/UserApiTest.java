import com.softserveinc.ita.homeproject.ApiClient;
import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ServerConfiguration;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

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

    @Test
    public void getAllUsersTest () throws ApiException {
        List<ReadUser> readUsers = userApi.queryUsers(1, 10);
    }


    @Test
    public void getUserTest () {

    }

}
