package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.application.mapper.ViewMapper;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.*;

/**
 * UserApiServiceImpl class is the inter layer between generated
 * User controller and service layer of the application.
 *
 * @author Mykyta Morar
 */
@Service
@RequiredArgsConstructor
public class UserApiServiceImpl implements UsersApiService {

    private final UserService userService;
    private final ViewMapper mapper;

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
        UserDto createUserDto =  mapper.convertToDto(createUser, new UserDto());
        UserDto readUserDto = userService.createUser(createUserDto);
        ReadUser readUser = mapper.convertToView(readUserDto, new ReadUser());

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
        ReadUser readUser = mapper.convertToView(readUserDto, new ReadUser());

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    /**
     * queryUsers method is implementation of HTTP GET
     * method for getting all users from database.
     *
     * @param pageNumber is the number of returned page with elements
     * @param pageSize is amount of the returned elements
     * @return Response to generated controller
     */
    @PreAuthorize(GET_ALL_USERS_PERMISSION)
    @Override
    public Response queryUsers(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {
        List<ReadUser> readUserList = userService.getAllUsers(pageNumber, pageSize).stream()
                .map((userDto) -> mapper.convertToView(userDto, new ReadUser()))
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readUserList).build();
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

    /**
     * updateUser method is implementation of HTTP PUT
     * method for updating existing user.
     *
     * @param id is id of the user that has to be updated
     * @param updateUser are incoming data needed for user's update
     * @return Response to generated controller
     */
    @PreAuthorize(UPDATE_USER_PERMISSION)
    @Override
    public Response updateUser(Long id, UpdateUser updateUser) {
        UserDto updateUserDto = mapper.convertToDto(updateUser, new UserDto());
        UserDto readUserDto = userService.updateUser(id, updateUserDto);
        ReadUser readUser = mapper.convertToView(readUserDto, new ReadUser());

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

}
