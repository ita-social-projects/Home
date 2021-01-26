package com.softserveinc.ita.homeproject.application.apiservice;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.application.mapper.CreateUserDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.DtoToViewMapper;
import com.softserveinc.ita.homeproject.application.mapper.ReadUserDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.UpdateUserDtoMapper;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homeservice.query.impl.UserQueryConfig.UserQueryParamEnum;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_USER_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DEACTIVATE_USER_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_USERS_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_USER_BY_ID_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_USER_PERMISSION;

/**
 * UserApiServiceImpl class is the inter layer between generated
 * User controller and service layer of the application.
 *
 * @author Mykyta Morar
 */
@Service
@RequiredArgsConstructor
public class UserApiServiceImpl extends CommonApiService<UserDto> implements UsersApiService {

    private final UserService userService;
    private final CreateUserDtoMapper createUserDtoMapper;
    private final ReadUserDtoMapper readUserDtoMapper;
    private final UpdateUserDtoMapper updateUserDtoMapper;
    private final EntitySpecificationService<User> entitySpecificationService;

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
        UserDto createUserDto = createUserDtoMapper.convertViewToDto(createUser);
        UserDto readUserDto = userService.createUser(createUserDto);
        ReadUser readUser = readUserDtoMapper.convertDtoToView(readUserDto);

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
        ReadUser readUser = readUserDtoMapper.convertDtoToView(readUserDto);

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
    public Response queryUsers(@Min(1) Integer pageNumber,
                               @Min(0) @Max(10) Integer pageSize,
                               String sort,
                               String search,
                               String id,
                               String email,
                               String firstName,
                               String lastName,
                               String contact) {

        Map<QueryParamEnum, String> filterMap = new HashMap<>();

        filterMap.put(UserQueryParamEnum.ID, id);
        filterMap.put(UserQueryParamEnum.EMAIL, email);
        filterMap.put(UserQueryParamEnum.CONTACT, contact);
        filterMap.put(UserQueryParamEnum.LAST_NAME, lastName);
        filterMap.put(UserQueryParamEnum.FIRST_NAME, firstName);

        Page<UserDto> users = userService.findUsers(
                pageNumber,
                pageSize,
                entitySpecificationService.getSpecification(filterMap, search, sort)
        );

        return buildQueryResponse(users);
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
     * @param id         is id of the user that has to be updated
     * @param updateUser are incoming data needed for user's update
     * @return Response to generated controller
     */
    @PreAuthorize(UPDATE_USER_PERMISSION)
    @Override
    public Response updateUser(Long id, UpdateUser updateUser) {
        UserDto updateUserDto = updateUserDtoMapper.convertViewToDto(updateUser);
        UserDto readUserDto = userService.updateUser(id, updateUserDto);
        ReadUser readUser = readUserDtoMapper.convertDtoToView(readUserDto);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    @Override
    public DtoToViewMapper<UserDto, ?> getDtoToViewMapper() {
        return readUserDtoMapper;
    }
}
