package myapp.api;

import myapp.Validations;
import myapp.model.Device;
import myapp.snmp.SNMPService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("snmp")
public class SnmpResource {

    Device one = new Device("one");
    Device two = new Device("two");
    Device three = new Device("three");
    List list = new ArrayList();

//    @GET
//    @Path("hello")
//    public Response sayHello() {
//        return Response.status(Response.Status.OK).entity("Hello!!!!").build();
//    }
//
//    @GET
//    @Path("device/{name}")
//    public Response getDevice(@PathParam("name") String name) {
//        list.add(one);
//        list.add(two);
//        list.add(three);
//        Device tmp = new Device(name);
//        if (list.contains(tmp)) return Response.ok(list.get(list.indexOf(tmp)), MediaType.APPLICATION_JSON).build();
//        return Response.ok("Not found").build();
//    }
//
//    @GET
//    @Path("device")
//    public Response getAllDevices() {
//        list.add(one);
//        list.add(two);
//        list.add(three);
//        return Response.ok(list, MediaType.APPLICATION_JSON).build();
//    }

    @POST
    @Path("ask")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getOid(List<String> body) {
        if (body.stream().filter(oidString -> !Validations.isValidOID(oidString)).collect(Collectors.toList())
                .size() > 0) return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.ok(SNMPService.getPduResults(body)).build();
    }



}
