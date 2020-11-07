package com.softserveinc.ita.homeproject.services;

import com.softserveinc.ita.homeproject.api.ApiResponseMessage;
import com.softserveinc.ita.homeproject.api.NotFoundException;
import com.softserveinc.ita.homeproject.api.UsersApiService;
import com.softserveinc.ita.homeproject.model.PostUser;
import com.softserveinc.ita.homeproject.model.User;
import com.softserveinc.ita.homeproject.model.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Service
public class UserApiServiceImpl implements UsersApiService {

    private final UserRepository userRepository;

    public UserApiServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response createUser(PostUser postUser, SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response login(PostUser postUser, SecurityContext securityContext) throws NotFoundException {
        User user = userRepository.findByEmail(postUser.getEmail()).orElseThrow();
        // do some magic!
        return Response.ok().entity(user).build();
    }
    @Override
    public Response getUserDetails(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response usersLogoutDelete(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
