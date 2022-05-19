//package readerapp.readercontroller;
//
//import org.springframework.web.bind.annotation.RequestBody;
//import readerapp.readermodels.UserForMongo;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.Response;
//
//@Path("/usersRead")
//public interface UserApiReader {
//
//    @GET
//    @Path("/{id}")
//    @Produces({ "application/json" })
//    Response getUser(@PathParam("id") Long id);
//
//    @POST
//    @Consumes({ "application/json" })
//    @Produces({ "application/json" })
//    @Path("/create")
//    Response createUser(@RequestBody UserForMongo createUser);
//}
