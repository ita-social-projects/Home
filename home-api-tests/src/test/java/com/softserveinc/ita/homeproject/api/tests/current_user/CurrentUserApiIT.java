package com.softserveinc.ita.homeproject.api.tests.current_user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CurrentUserApi;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import org.junit.jupiter.api.Test;

class CurrentUserApiIT {

    private final CurrentUserApi currentUserApi = new CurrentUserApi(ApiClientUtil.getCooperationAdminClient());

    @Test
    void getCurrentUser() throws ApiException {
        ReadUser user = currentUserApi.getCurrentUser();
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
        assertNotNull(user.getMiddleName());
        assertNotNull(user.getEmail());
        assertNotNull(user.getContacts());
        assertNotNull(user.getId());
    }
}
