package com.m3.methodcache.interceptor;

import com.m3.memcached.facade.MemcachedClient;
import com.m3.memcached.facade.MemcachedClientPool;
import com.m3.methodcache.annotation.CacheResult;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import com.m3.methodcache.interceptor.MemcachedCacheResultInterceptor.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

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

    public String getSomething(String key, String value) throws Exception {
        return null;
    }

    @Test
    public void getCacheKey_A$MethodInvocation$boolean_raw() throws Exception {
        MemcachedCacheResultInterceptor interceptor = new MemcachedCacheResultInterceptor();
        MethodInvocation invocation = mock(MethodInvocation.class);
        Method method = this.getClass().getMethod("getSomething", String.class, String.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] { "foo", "bar" });
        String actual = interceptor.getCacheKey(invocation, true);
        String expected = "com.m3.methodcache.interceptor.MemcachedCacheResultInterceptorTest#getSomething" +
                "(java.lang.String,java.lang.String)" +
                " throws java.lang.Exception_$$_" +
                "foo_$_bar_$_";
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void getCacheKey_A$MethodInvocation$boolean_hash() throws Exception {
        MemcachedCacheResultInterceptor interceptor = new MemcachedCacheResultInterceptor();
        MethodInvocation invocation = mock(MethodInvocation.class);
        Method method = this.getClass().getMethod("getSomething", String.class, String.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] { "foo", "bar" });
        String actual = interceptor.getCacheKey(invocation, false);
        String expected = "c0fdd32fbd47bfc945c2404725172b6b";
        assertThat(actual, is(equalTo(expected)));
    }

	@Test
	public void getConfiguration_A$() throws Exception {
		MemcachedCacheResultInterceptor interceptor = new MemcachedCacheResultInterceptor();
		Configuration actual = interceptor.getConfiguration();
		assertThat(actual, is(nullValue()));
	}

    static class Foo {
        public void doSomething() {

        }
    }

	@Test
	public void invoke_A$MethodInvocation() throws Throwable {
		MemcachedCacheResultInterceptor interceptor = new MemcachedCacheResultInterceptor();
		MethodInvocation invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(Foo.class.getDeclaredMethod("doSomething"));
        when(invocation.proceed()).thenReturn("foo");
		Object result = interceptor.invoke(invocation);
		Object expected = "foo";
		assertThat(result, is(equalTo(expected)));
	}

}
