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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * CacheResultInterceptor using Memcached
 */
public class MemcachedCacheResultInterceptor extends AbstractCacheResultInterceptor implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(MemcachedCacheResultInterceptor.class);

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
            Object cachedObject = null;
            try {
                cachedObject = memcached.get(cacheKey);
            } catch (Throwable t) {
                log.debug("Failed to get a value from memcached servers. (" + cacheKey + ")", t);
            }
            if (cachedObject != null) {
                return cachedObject;
            } else {
                Object value = invocation.proceed();
                try {
                    memcached.set(cacheKey, annotation.secondsToExpire(), value);
                } catch (Throwable t) {
                    log.debug("Failed to set a value to memcached servers. (" + cacheKey + " -> " + value + ")", t);
                }
                return value;
            }
        }
        return invocation.proceed();
    }

}


