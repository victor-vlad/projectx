package myapp.api;

import myapp.model.Device;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("snmp")
public class SnmpResource {

    @GET
    @Path("hello")
    public Response sayHello() {
        return Response.status(Response.Status.OK).entity("Hello! 0").build();
//        Response.ok("Hello! 1").build();
    }

    @GET
    @Path("device/{name}")
    public Response getDevice(@PathParam("name") String name) {
        Device d1 = new Device("d1");
        if (name.equals("ceva")) return Response.ok(d1, MediaType.APPLICATION_JSON).build();
        return Response.status(404).build();
    }

}
