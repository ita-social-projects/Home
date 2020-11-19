package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NotFoundException;
import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Service
@RequiredArgsConstructor
public class UserApiServiceImpl implements UsersApiService {

    private final UserServiceImpl userServiceImpl;

    @PreAuthorize("hasAuthority('CREATE_USER_PERMISSION')")
    @Override
    public Response createUser(CreateUser createUser, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.create(createUser)).build();
    }

    @PreAuthorize("hasAuthority('UPDATE_USER_PERMISSION')")
    @Override
    public Response updateUser(Integer id, UpdateUser updateUser, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.update(id.longValue(), updateUser)).build();
    }

    @PreAuthorize("hasAuthority('GET_USERS_PERMISSION')")
    @Override
    public Response usersGet(@Min(0) Integer offset, @Min(1) @Max(100) Integer limit, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.getAll()).build();
    }

    @PreAuthorize("hasAuthority('DELETE_USER_PERMISSION')")
    @Override
    public Response usersIdDelete(Integer id, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.deleteById(id.longValue())).build();
    }

    @PreAuthorize("hasAuthority('GET_USER_PERMISSION')")
    @Override
    public Response usersIdGet(Integer id, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.getById(id.longValue())).build();
    }
}
