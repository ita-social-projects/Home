package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_USER_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_USER_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_USER_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_USERS_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_USER_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_USER_BY_ID_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_USER_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_USER_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_USER_PERMISSION;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.UsersApi;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.UserContactService;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateContact;
import com.softserveinc.ita.homeproject.model.UpdateUser;
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

    @PreAuthorize(CREATE_CONTACT_PERMISSION)
    @Override
    public Response createContactOnUser(Long userId, @Valid CreateContact createContact) {
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
    @PreAuthorize(CREATE_USER_PERMISSION)
    @Override
    public Response createUser(CreateUser createUser) {
        UserDto createUserDto = mapper.convert(createUser, UserDto.class);
        UserDto readUserDto = userService.createUser(createUserDto);
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.CREATED).entity(readUser).build();
    }

    /**
     * getUser method is implementation of HTTP GET method
     * for getting user by id from database.
     *
     * @param id is user's id in the database
     * @return Response to generated controller
     */
    @PreAuthorize(GET_USER_BY_ID_PERMISSION)
    @Override
    public Response getUser(Long id) {
        UserDto readUserDto = userService.getOne(id);
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    @PreAuthorize(GET_ALL_USER_CONTACT_PERMISSION)
    @Override
    public Response queryContactsOnUser(Long userId,
                                        @Min(1) Integer pageNumber,
                                        @Min(1) @Max(10) Integer pageSize,
                                        String sort,
                                        String filter,
                                        Long id,
                                        String phone,
                                        String email,
                                        String main,
                                        ContactType type) {

        Page<ContactDto> contacts = contactService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(contacts, ReadContact.class);
    }

    @PreAuthorize(DELETE_USER_CONTACT_PERMISSION)
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
    @PreAuthorize(GET_ALL_USERS_PERMISSION)
    @Override
    public Response getAllUsers(@Min(1) Integer pageNumber,
                                @Min(1) @Max(10) Integer pageSize,
                                String sort,
                                String filter,
                                Long id,
                                String email,
                                String firstName,
                                String lastName,
                                String contactPhone,
                                String contactEmail) {

        Page<UserDto> users = userService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(users, ReadUser.class);
    }

    @PreAuthorize(GET_USER_CONTACT_PERMISSION)
    @Override
    public Response getContactOnUser(Long userId, Long id) {
        ContactDto readContactDto = contactService.getOne(id, getSpecification());
        ReadContact readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
    }

    /**
     * removeUser method is implementation of HTTP DELETE
     * method for deactivating user's account.
     *
     * @param id is id of the user that has to be deactivated
     * @return Response to generated controller
     */
    @PreAuthorize(DELETE_USER_PERMISSION)
    @Override
    public Response deleteUser(Long id) {
        userService.deactivateUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(UPDATE_USER_CONTACT_PERMISSION)
    @Override
    public Response updateContactOnUser(Long userId, Long id, @Valid UpdateContact updateContact) {
        var updateContactDto = mapper.convert(updateContact, ContactDto.class);
        var readContactDto = contactService.updateContact(userId, id, updateContactDto);
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
    @PreAuthorize(UPDATE_USER_PERMISSION)
    @Override
    public Response updateUser(Long id, UpdateUser updateUser) {
        UserDto updateUserDto = mapper.convert(updateUser, UserDto.class);
        UserDto readUserDto = userService.updateUser(id, updateUserDto);
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }
}
