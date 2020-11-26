package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.UserService;
import com.softserveinc.ita.homeproject.service.dto.CreateUserDto;
import com.softserveinc.ita.homeproject.service.dto.ReadUserDto;
import com.softserveinc.ita.homeproject.service.dto.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeproject.service.constants.Permissions.*;

@Service
@RequiredArgsConstructor
public class UserApiServiceImpl implements UsersApiService {

    private final UserService userService;
    private final ConversionService conversionService;

    @PreAuthorize(CREATE_USER_PERMISSION)
    @Override
    public Response createUser(CreateUser createUser, SecurityContext securityContext) throws NotFoundException {
        CreateUserDto createUserDto = conversionService.convert(createUser, CreateUserDto.class);
        ReadUserDto readUserDto = userService.createUser(createUserDto);
        ReadUser readUser = conversionService.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.CREATED).entity(readUser).build();
    }

    @PreAuthorize(GET_USER_BY_ID_PERMISSION)
    @Override
    public Response getUser(Long id, SecurityContext securityContext) throws NotFoundException {
        ReadUserDto readUserDto = userService.getUserById(id);
        ReadUser readUser = conversionService.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

    @PreAuthorize(GET_ALL_USERS_PERMISSION)
    @Override
    public Response queryUsers(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize, SecurityContext securityContext) throws NotFoundException {
        List<ReadUser> readUserList = userService.getAllUsers(pageNumber, pageSize).stream()
                .map(readUserDto -> conversionService.convert(readUserDto, ReadUser.class))
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readUserList).build();
    }

    @PreAuthorize(DEACTIVATE_USER_PERMISSION)
    @Override
    public Response removeUser(Long id, SecurityContext securityContext) throws NotFoundException {
        userService.deactivateUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(UPDATE_USER_PERMISSION)
    @Override
    public Response updateUser(Long id, UpdateUser updateUser, SecurityContext securityContext) throws NotFoundException {
        UpdateUserDto updateUserDto = conversionService.convert(updateUser, UpdateUserDto.class);
        ReadUserDto readUserDto = userService.updateUser(id, updateUserDto);
        ReadUser readUser = conversionService.convert(readUserDto, ReadUser.class);

        return Response.status(Response.Status.OK).entity(readUser).build();
    }

}
