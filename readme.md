# Method Cache Interceptor

## Getting Started

### Maven

```xml
<dependencies>
  <dependency>
    <groupId>com.m3</groupId>
    <artifactId>method-cache-interceptor</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

## Usage

### Setup for Spring Framework

"applicationContext.xml" as follows:

```xml
<bean id="memcachedConfiguration" class="com.m3.memcached.facade.Configuration">
  <property name="namespace" value="com.m3.example" />
  <property name="adaptorClassName" value="com.m3.memcached.facade.adaptor.SpymemcachedAdaptor" />
  <property name="addressesAsString" value="127.0.0.1:11211,127.0.0.1:11212" />
</bean>

<bean id="memcachedCacheResultInterceptor" class="com.m3.methodcache.interceptor.MemcachedCacheResultInterceptor"/>

<aop:config>
  <aop:advisor advice-ref="memcachedCacheResultInterceptor" pointcut="..."/>
</aop:config>
```

### Setup by inheritence

It's also possible to inject the configuration to interceptor by inheritence.

```java
public class MyMemcachedInterceptor extends memcachedCacheResultInterceptor {

  @Override
  public Configuration getConfiguration() {
    Configuration config = new Configuration();
    config.setNamespace("....");
    ...
    return config;
  }

}
```

### Using AOP

The value will be cached for the duration of 10 seconds.

```java
package service;

import com.m3.methodcache.annotation.CacheResult;

public class DateService {

  @CacheResult(secondsToExpire = 10)
  public String getCurrentAsString(String prefix) {
    return prefix + new java.util.Date().toString();
  }

}
```

### The rule of generated cache key

```
"(namespace)::(Method#toGenericString() and replace "\\s+" to "_")::(args separated by comma)"
```

For example:

```java
String result = new DateService().getCurrentAsString("PREFIX");
```

And thn, the returned value will be cached as "com.example::public_void_service.DateService.getCurrentAsString(String)::PREFIX" on memcached.


## License

```
 Copyright 2011 - 2012 M3, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific language
 governing permissions and limitations under the License.
```


