package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_USER_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DEACTIVATE_USER_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_USERS_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_USER_BY_ID_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_USER_PERMISSION;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.UsersApi;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.ContactService;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
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
    private ContactService contactService;

    @Autowired
    private UserService userService;

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
        UserDto readUserDto = userService.getUserById(id);
        ReadUser readUser = mapper.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    @Override
    public Response queryContactsOnUser(Long userId,
                                        @Min(1) Integer pageNumber,
                                        @Min(0) @Max(10) Integer pageSize,
                                        String sort,
                                        String filter,
                                        String id,
                                        String phone,
                                        String email,
                                        String main,
                                        String type) {
        Map<String, String> filterMap = new HashMap<>();

        filterMap.put("user_id", userId.toString());
        filterMap.put("id", id);
        filterMap.put("phone", phone);
        filterMap.put("email", email);
        filterMap.put("main", main);
        filterMap.put("type", type);

        Page<ContactDto> contacts = contactService.getAllContacts(
            pageNumber,
            pageSize,
            entitySpecificationService.getSpecification(filterMap, filter, sort)
        );

        return buildQueryResponse(contacts, ReadContact.class);
    }

    @Override
    public Response deleteContactOnUser(Long userId, Long contactId) {
        contactService.deactivateContact(contactId);

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
                                @Min(0) @Max(10) Integer pageSize,
                                String sort,
                                String filter,
                                String id,
                                String email,
                                String firstName,
                                String lastName,
                                String contactPhone,
                                String contactEmail) {
        Map<String, String> filterMap = new HashMap<>();

        filterMap.put("id", id);
        filterMap.put("email", email);
        filterMap.put("lastName", lastName);
        filterMap.put("firstName", firstName);
        filterMap.put("contactPhone", contactPhone);
        filterMap.put("contactEmail", contactEmail);

        Page<UserDto> users = userService.findUsers(
            pageNumber,
            pageSize,
            entitySpecificationService.getSpecification(filterMap, filter, sort)
        );

        return buildQueryResponse(users, ReadUser.class);
    }

    @Override
    public Response getContactOnUser(Long userId, Long contactId) {
        ContactDto readContactDto = contactService.getContactById(contactId);
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
    @PreAuthorize(DEACTIVATE_USER_PERMISSION)
    @Override
    public Response deleteUser(Long id) {
        userService.deactivateUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response updateContactOnUser(Long userId, Long contactId, @Valid UpdateContact updateContact) {
        ContactDto updateContactDto = mapper.convert(updateContact, ContactDto.class);
        ContactDto readContactDto = contactService.updateContact(contactId, updateContactDto);
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
