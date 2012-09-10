package com.m3.methodcache.interceptor;

import com.m3.memcached.facade.Configuration;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;

public class MemcachedCacheResultInterceptorTest {

    Configuration config = null;
    InetSocketAddress address = null;

    @Before
    public void setUp() throws Exception {
        if (config == null) {
            config = Configuration.loadConfigFromProperties();
        }
        address = config.getAddresses().get(0);
    }

    @Test
    public void type() throws Exception {
        assertThat(MemcachedCacheResultInterceptor.class, notNullValue());
    }

    @Test
    public void instantiation() throws Exception {
        MemcachedCacheResultInterceptor target = new MemcachedCacheResultInterceptor();
        assertThat(target, notNullValue());
    }

    @Test
    public void getCacheKey_A$MethodInvocation() throws Exception {
        MemcachedCacheResultInterceptor interceptor = new MemcachedCacheResultInterceptor();
        MethodInvocation invocation = mock(MethodInvocation.class);
        Method method = this.getClass().getMethod("getCacheKey_A$MethodInvocation", (Class<?>[]) null);
        when(invocation.getMethod()).thenReturn(method);
        String actual = interceptor.getCacheKey(invocation);
        String expected = "public void com.m3.methodcache.interceptor.MemcachedCacheResultInterceptorTest.getCacheKey_A$MethodInvocation() throws java.lang.Exception::";
        assertThat(actual, is(equalTo(expected)));
    }

}
