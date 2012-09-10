package com.m3.methodcache.example.jerseytest;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MultivaluedMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class RootResourceTest extends JerseyTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("restful.server.resource")
                .contextParam("contextConfigLocation", "classpath:/applicationContext.xml")
                .contextPath("/")
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .build();
    }

    @Test
    public void index() throws Exception {
        WebResource webResource = resource();
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("q", "Java");
        Thread.sleep(3000L);
        String res1 = webResource.path("/").queryParams(params).get(String.class);
        Thread.sleep(1000L);
        String res2 = webResource.path("/").queryParams(params).get(String.class);
        Thread.sleep(2000L);
        String res3 = webResource.path("/").queryParams(params).get(String.class);

        assertThat(res2, is(equalTo(res1)));
        assertThat(res3, is(not(equalTo(res1))));
    }

}
