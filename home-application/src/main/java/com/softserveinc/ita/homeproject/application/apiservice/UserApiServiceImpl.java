package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.application.mapper.CreateUserDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.ReadUserDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.UpdateUserDtoMapper;
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
 * UserApiServiceImpl class is the interlayer between generated
 * User controller and service layer of the application.
 *
 * @author Mykyta Morar
 */
@Service
@RequiredArgsConstructor
public class UserApiServiceImpl implements UsersApiService {

    private final UserService userService;
    private final CreateUserDtoMapper createUserDtoMapper;
    private final ReadUserDtoMapper readUserDtoMapper;
    private final UpdateUserDtoMapper updateUserDtoMapper;

    /**
     * createUser method is implementation of HTTP POST
     * method, that is used to create a new user.
     *
     * @param createUser incoming data needed for creation of user
     * @return returns Response to generated controller
     */
    @PreAuthorize(CREATE_USER_PERMISSION)
    @Override
    public Response createUser(CreateUser createUser) {
        UserDto createUserDto = createUserDtoMapper.convertViewToDto(createUser);
        UserDto readUserDto = userService.createUser(createUserDto);
        ReadUser readUser = readUserDtoMapper.convertDtoToView(readUserDto);

        return Response.status(Response.Status.CREATED).entity(readUser).build();
    }

    /**
     * getUser method is implementation of HTTP GET method,
     * that is used to get one user by id from database.
     *
     * @param id id of the user in the database
     * @return returns Response to generated controller
     */
    @PreAuthorize(GET_USER_BY_ID_PERMISSION)
    @Override
    public Response getUser(Long id) {
        UserDto readUserDto = userService.getUserById(id);
        ReadUser readUser = readUserDtoMapper.convertDtoToView(readUserDto);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    /**
     * queryUsers method is implementation of HTTP GET
     * method, that is used to get all users from database.
     *
     * @param pageNumber number of the returned page with elements
     * @param pageSize amount of the returned elements
     * @return returns Response to generated controller
     */
    @PreAuthorize(GET_ALL_USERS_PERMISSION)
    @Override
    public Response queryUsers(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {
        List<ReadUser> readUserList = userService.getAllUsers(pageNumber, pageSize).stream()
                .map(readUserDtoMapper::convertDtoToView)
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readUserList).build();
    }

    /**
     * removeUser method is implementation of HTTP DELETE
     * method, that is used to deactivate user's account.
     *
     * @param id id of the user that needs to be deactivated
     * @return returns Response to generated controller
     */
    @PreAuthorize(DEACTIVATE_USER_PERMISSION)
    @Override
    public Response removeUser(Long id) {
        userService.deactivateUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * updateUser method is implementation of HTTP PUT
     * method, that is update the existing user.
     *
     * @param id id of the user that needs to be updated
     * @param updateUser incoming data needed for user's update
     * @return returns Response to generated controller
     */
    @PreAuthorize(UPDATE_USER_PERMISSION)
    @Override
    public Response updateUser(Long id, UpdateUser updateUser) {
        UserDto updateUserDto = updateUserDtoMapper.convertViewToDto(updateUser);
        UserDto readUserDto = userService.updateUser(id, updateUserDto);
        ReadUser readUser = readUserDtoMapper.convertDtoToView(readUserDto);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

}
