package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.UsersApi;
import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.ContactService;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
import com.softserveinc.ita.homeproject.model.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.*;

/**
 * UserApiServiceImpl class is the inter layer between generated
 * User controller and service layer of the application.
 *
 * @author Mykyta Morar
 */
@Provider
@NoArgsConstructor
public class UserApiImpl implements UsersApi {

    private UserService userService;
    private HomeMapper mapper;
    private ContactService contactService;

    @Override
    public Response createContacts(Long usersId, @Valid CreateContact createContact) {
        ContactDto createContactDto = mapper.convert(createContact, ContactDto.class);
        ContactDto readContactDto = contactService.createContact(usersId, createContactDto);
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

    @Override
    public Response getAllContacts(Long usersId, @Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {

        List<ReadContact> readContactList = contactService.getAllContacts(usersId, pageNumber, pageSize).stream()
                .map(contactDto -> mapper.convert(contactDto, ReadContact.class))
                .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(readContactList).build();
    }

    @Override
    public Response getContact(Long usersId, Long contactsId) {
        ContactDto readContactDto = contactService.getContactById(contactsId);
        ReadContact readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
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
    public Response queryUsers(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {
        List<ReadUser> readUserList = userService.getAllUsers(pageNumber, pageSize).stream()
                .map((userDto) -> mapper.convert(userDto, ReadUser.class))
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readUserList).build();
    }

    @Override
    public Response removeContact(Long usersId, Long contactsId) {
        contactService.deactivateContact(contactsId);

        return Response.status(Response.Status.NO_CONTENT).build();
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
    public Response removeUser(Long id) {
        userService.deactivateUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response updateContact(Long usersId, Long contactsId, @Valid UpdateContact updateContact) {
        ContactDto updateContactDto = mapper.convert(updateContact, ContactDto.class);
        ContactDto readContactDto = contactService.updateContact(contactsId, updateContactDto);
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

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMapper(HomeMapper mapper) {
        this.mapper = mapper;
    }
}
