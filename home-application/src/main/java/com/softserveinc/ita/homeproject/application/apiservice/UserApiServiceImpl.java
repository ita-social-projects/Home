package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NotFoundException;
import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.createUserPermission;

@Service
@RequiredArgsConstructor
public class UserApiServiceImpl implements UsersApiService {

    private final UserService userService;

    @PreAuthorize("hasAuthority('CREATE_USER_PERMISSION')")
    @Override
    public Response createUser(CreateUser createUser, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userService.createUser(createUser)).build();
    }

    @PreAuthorize("hasAuthority('UPDATE_USER_PERMISSION')")
    @Override
    public Response updateUser(Integer id, UpdateUser updateUser, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userService.updateUser(id.longValue(), updateUser)).build();
    }

    @PreAuthorize("hasAuthority('GET_USERS_PERMISSION')")
    @Override
    public Response usersGet(@Min(0) Integer offset, @Min(1) @Max(100) Integer limit, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userService.getAllUsers()).build();
    }

    @PreAuthorize("hasAuthority('DELETE_USER_PERMISSION')")
    @Override
    public Response usersIdDelete(Integer id, SecurityContext securityContext) throws NotFoundException {
        userService.deactivateUser(id.longValue());
        return Response.ok().entity(Response.status(Response.Status.OK)).build();
    }

    @PreAuthorize("hasAuthority('GET_USER_PERMISSION')")
    @Override
    public Response usersIdGet(Integer id, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userService.getUserById(id.longValue())).build();
    }
}
