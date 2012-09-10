/*
 * Copyright 2011 - 2012 M3, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.m3.methodcache.interceptor;

import com.m3.memcached.facade.Configuration;
import com.m3.memcached.facade.MemcachedClient;
import com.m3.memcached.facade.MemcachedClientPool;
import com.m3.methodcache.annotation.CacheResult;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Resource;

/**
 * CacheResultInterceptor using Memcached
 */
public class MemcachedCacheResultInterceptor extends AbstractCacheResultInterceptor implements MethodInterceptor {

    /**
     * If necessary, override this to provider {@link Configuration} instance.
     *
     * @return Configuration
     */
    public Configuration getConfiguration() {
        return null;
    }

    /**
     * Memcached configuration
     */
    @Resource
    private Configuration config = getConfiguration();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        CacheResult annotation = getCacheResultAnnotation(invocation);
        if (annotation != null) {
            MemcachedClient memcached = MemcachedClientPool.getMemcachedClient(config);
            String cacheKey = annotation.cacheKey().equals("") ? getCacheKey(invocation) : annotation.cacheKey();
            Object cachedObject = memcached.get(cacheKey);
            if (cachedObject != null) {
                return cachedObject;
            } else {
                Object value = invocation.proceed();
                memcached.set(cacheKey, annotation.secondsToExpire(), value);
                return value;
            }
        }
        return invocation.proceed();
    }

}
