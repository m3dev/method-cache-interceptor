package com.m3.methodcache.example.resource;

import com.m3.methodcache.example.BasicSpringTestCase;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RootResourceTest extends BasicSpringTestCase {

    @Resource
    RootResource resource;

    @Test
    public void instantiation() throws Exception {
        assertThat(resource, notNullValue());
    }

    @Test
    public void index() throws Exception {
        Thread.sleep(3000L);
        String res1 = resource.index();
        Thread.sleep(1000L);
        String res2 = resource.index();
        Thread.sleep(2000L);
        String res3 = resource.index();

        assertThat(res2, is(equalTo(res1)));
        assertThat(res3, is(not(equalTo(res1))));
    }

}
