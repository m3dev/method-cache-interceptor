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

import com.m3.methodcache.annotation.CacheResult;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Base implementation for CacheResultInterceptor
 */
public abstract class AbstractCacheResultInterceptor implements MethodInterceptor {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Using raw key
     * NOTE: memcached doesn't accept key which is longer than 250 bytes.
     */
    private boolean usingRawKey = false;

    /**
     * Returns {@link CacheResult} annotation if exists
     *
     * @param invocation method invocation
     * @return annotation instance
     */
    protected CacheResult getCacheResultAnnotation(MethodInvocation invocation) {
        Method invokedMethod = invocation.getMethod();
        return invokedMethod.getAnnotation(CacheResult.class);
    }

    /**
     * Returns using raw key
     *
     * @return using raw key
     */
    public boolean isUsingRawKey() {
        return usingRawKey;
    }

    /**
     * Set using raw key
     *
     * @param usingRawKey using raw key
     */
    public void setUsingRawKey(boolean usingRawKey) {
        this.usingRawKey = usingRawKey;
    }

    /**
     * Returns cache key
     *
     * @param invocation method invocation
     * @param raw        is raw
     * @return cache key
     */
    protected String getCacheKey(MethodInvocation invocation, boolean raw) {
        Method invokedMethod = invocation.getMethod();
        String signature = getClassPartOnly(invokedMethod) + "#" + getMethodPartOnly(invokedMethod) + "_$$_";
        String args = getArgsPart(invocation.getArguments()).toString();
        String rawKey = signature + args;
        if (log.isDebugEnabled()) {
            log.debug("Raw key: " + rawKey);
        }
        return raw ? rawKey : DigestUtils.md5Hex(rawKey);
    }

    protected static String getArgsPart(Object[] args) {
        StringBuilder sb = new StringBuilder();
        if (args != null) {
            for (Object arg : args) {
                sb.append(arg);
                sb.append("_$_");
            }
        }
        return sb.toString();
    }

    /**
     * Returns class part only from Method object
     *
     * @param invokedMethod method
     * @return class part only
     */
    protected static String getClassPartOnly(Method invokedMethod) {
        return getClassNameWithoutS2AOP(invokedMethod.getDeclaringClass().getCanonicalName());
    }

    /**
     * Removes S2AOP enhancement
     *
     * @param canonicalName canonical name
     * @return raw class name
     * @see "http://www.seasar.org/en/"
     */
    protected static String getClassNameWithoutS2AOP(String canonicalName) {
        return canonicalName.replaceFirst("\\$\\$EnhancedByS2AOP\\$\\$.+", "");
    }

    /**
     * Extracts method part only from method#toGenericString
     *
     * @param invokedMethod
     * @return method part only
     */
    protected static String getMethodPartOnly(Method invokedMethod) {
        return invokedMethod.toGenericString().replaceFirst("[^\\(]+?\\.([^\\(\\.]+?\\(.+)", "$1");
    }

}
