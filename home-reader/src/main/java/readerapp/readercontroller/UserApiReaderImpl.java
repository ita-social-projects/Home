package readerapp.readercontroller;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;
import readerapp.readermodels.UserForMongo;
import readerapp.readerservice.UserReaderServise;

@Provider
@Component
public class UserApiReaderImpl implements UserApiReader{

    private UserReaderServise userReaderServise;

    @Override
    public Response getUser(Long id) {
        userReaderServise.findAll();
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response createUser(UserForMongo createUser) {

        return Response.status(Response.Status.CREATED).build();
    }
}
