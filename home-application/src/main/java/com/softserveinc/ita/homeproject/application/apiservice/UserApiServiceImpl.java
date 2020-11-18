package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.NotFoundException;
import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequiredArgsConstructor
public class UserApiServiceImpl implements UsersApiService {

    private final UserServiceImpl userServiceImpl;

    @Override
    public Response createUser(CreateUser createUser, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.create(createUser)).build();
    }

    @Override
    public Response updateUser(Integer id, UpdateUser updateUser, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.update(id.longValue(), updateUser)).build();
    }

    @Override
    public Response usersGet(@Min(0) Integer offset, @Min(1) @Max(100) Integer limit, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.getAll()).build();
    }

    @Override
    public Response usersIdDelete(Integer id, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.deleteById(id.longValue())).build();
    }

    @Override
    public Response usersIdGet(Integer id, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(userServiceImpl.getById(id.longValue())).build();
    }
}
