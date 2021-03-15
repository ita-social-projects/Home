package com.softserveinc.ita.homeproject.api.tests.contacts;

import com.softserveinc.ita.homeproject.api.ContactApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreateUser;

public class ContactQueryIT {
    private final CreateEmailContact createEmailContact = new CreateEmailContact().email("useremail@example.com");
    private final CreateUser createUser = new CreateUser()
        .firstName("firstName")
        .lastName("lastName")
        .password("password")
        .addContactsItem(createEmailContact);

    private ContactApi contactApi;
    private UserApi userApi;
}
