package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_USER;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.ContactType;
import com.softserveinc.ita.homeproject.application.model.CreateContact;
import com.softserveinc.ita.homeproject.application.model.CreateUser;
import com.softserveinc.ita.homeproject.application.model.ReadContact;
import com.softserveinc.ita.homeproject.application.model.ReadPhoneContact;
import com.softserveinc.ita.homeproject.application.model.ReadUser;
import com.softserveinc.ita.homeproject.application.model.UpdateContact;
import com.softserveinc.ita.homeproject.application.model.UpdateUser;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.general.contact.user.UserContactService;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * UserApiServiceImpl class is the inter layer between generated
 * User controller and service layer of the application.
 *
 * @author Mykyta Morar
 */
@Provider
@Component
public class UserApiImpl extends CommonApi implements UsersApi {

    @Autowired
    private UserContactService contactService;

    @Autowired
    private UserService userService;

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response createContactOnUser(Long userId, CreateContact createContact) {
        ContactDto createContactDto = mapper.convert(createContact, ContactDto.class);
        ContactDto readContactDto = contactService.createContact(userId, createContactDto);
        ReadContact readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.CREATED).entity(readContact).build();
    }

    /**
     * createUser method is implementation of HTTP POST
     * method for creating a new user.
     *
     * @param createUser are incoming data needed for user's creation
     * @return Response to generated controller
     */
    @PermitAll
    @Override
    public Response createUser(CreateUser createUser) {
        ReadContact phone = new ReadPhoneContact()
            .phone("380501112233")
            .id(1L)
            .main(true)
            .type(ContactType.PHONE);
        ReadUser readUser = new ReadUser()
            .id(1L)
            .firstName("John")
            .middleName("Charles")
            .lastName("Doe")
            .email("mail@mailbox.com")
            .contacts(List.of(phone));
        return Response.status(Response.Status.CREATED).entity(readUser).build();
    }

    /**
     * getUser method is implementation of HTTP GET method
     * for getting user by id from database.
     *
     * @param id is user's id in the database
     * @return Response to generated controller
     */
    @PreAuthorize(MANAGE_USER)
    @Override
    public Response getUser(Long id) {
        UserDto readUserDto = userService.getOne(id);
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response queryContactsOnUser(Long userId,
                                        Integer pageNumber,
                                        Integer pageSize,
                                        String sort,
                                        String filter,
                                        Long id,
                                        String phone,
                                        String email,
                                        String main,
                                        ContactType type) {
        verifyExistence(userId, userService);
        Page<ContactDto> contacts = contactService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(contacts, ReadContact.class);
    }

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response deleteContactOnUser(Long userId, Long id) {
        contactService.deactivateContact(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * queryUsers method is implementation of HTTP GET
     * method for getting all users from database.
     *
     * @param pageNumber is the number of returned page with elements
     * @param pageSize   is amount of the returned elements
     * @return Response to generated controller
     */
    @PreAuthorize(MANAGE_USER)
    @Override
    public Response getAllUsers(Integer pageNumber,
                                Integer pageSize,
                                String sort,
                                String filter,
                                Long id,
                                String email,
                                String firstName,
                                String middleName,
                                String lastName,
                                String contactPhone,
                                String contactEmail) {

        Page<UserDto> users = userService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(users, ReadUser.class);
    }

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response getContactOnUser(Long userId, Long id) {
        ContactDto readContactDto = contactService.getOne(id, getSpecification());
        ReadContact readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
    }

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response getCurrentUser() {
        UserDto readUserDto = userService.getCurrentUser();
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);
        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    /**
     * removeUser method is implementation of HTTP DELETE
     * method for deactivating user's account.
     *
     * @param id is id of the user that has to be deactivated
     * @return Response to generated controller
     */
    @PreAuthorize(MANAGE_USER)
    @Override
    public Response deleteUser(Long id) {
        userService.deactivateUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(MANAGE_USER)
    @Override
    public Response updateContactOnUser(Long userId, Long id, UpdateContact updateContact) {
        ContactDto updateContactDto = mapper.convert(updateContact, ContactDto.class);
        ContactDto readContactDto = contactService.updateContact(userId, id, updateContactDto);
        ReadContact readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
    }

    /**
     * updateUser method is implementation of HTTP PUT
     * method for updating existing user.
     *
     * @param id         is id of the user that has to be updated
     * @param updateUser are incoming data needed for user's update
     * @return Response to generated controller
     */
    @PreAuthorize(MANAGE_USER)
    @Override
    public Response updateUser(Long id, UpdateUser updateUser) {
        UserDto updateUserDto = mapper.convert(updateUser, UserDto.class);
        UserDto readUserDto = userService.updateUser(id, updateUserDto);
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }
}
