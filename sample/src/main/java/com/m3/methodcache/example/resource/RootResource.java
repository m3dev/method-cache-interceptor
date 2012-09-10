package com.m3.methodcache.example.resource;

import com.m3.methodcache.annotation.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Date;

@Component
@Path("/")
public class RootResource {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @CacheResult(secondsToExpire = 2)
    @GET
    @Path("/")
    public String index() {
        log.info("index called!");
        return "Hello, JAX-RS(Jersey) with Spring! (" + new Date().toString() + ")";
    }

}
