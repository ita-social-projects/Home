package readerapp.readercontroller;

import readerapp.readermodels.UserForMongo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/usersRead")
public interface UserApiReader {

    @GET
    @Path("/{id}")
    @Produces({ "application/json" })
    Response getUser(@PathParam("id") Long id);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Response createUser(@Valid @NotNull UserForMongo createUser);
}
