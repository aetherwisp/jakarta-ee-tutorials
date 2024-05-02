package com.github.aetherwisp.jakartaee.tutorials.restfulwebservice;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("hello")
public class HelloResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public HelloRecord hello(@QueryParam("name") String _name) {
        if (null == _name || _name.trim().isEmpty()) {
            _name = "world";
        }

        return new HelloRecord(_name);
    }
}
