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

import java.lang.reflect.Method;

/**
 * Base implementation for CacheResultInterceptor
 */
public abstract class AbstractCacheResultInterceptor implements MethodInterceptor {

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
     * Returns cache key
     *
     * @param invocation method invocation
     * @return cache key
     */
    protected String getCacheKey(MethodInvocation invocation) {
        Object[] args = invocation.getArguments();
        StringBuilder argsBuffer = new StringBuilder();
        if (args != null) {
            for (Object arg : args) {
                argsBuffer.append(arg);
                argsBuffer.append(",");
            }
        }
        Method invokedMethod = invocation.getMethod();
        return invokedMethod.toGenericString() + "::" + argsBuffer.toString();
    }

}
