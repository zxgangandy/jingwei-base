# jingwei-base
jingwei-base


## idgen
- 使用mybatis plus 重构了百度uid generator

## feign
- feign的异常转换处理
- 基于base-utils
## codegen
- 基于mybatis plus的代码生成

## apollo

## lock
- 基于mysql的分布式锁实现
- 基于mybatis plus
## utils

## base-eureka
The eureka client lifecycle management tool.

[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zxgangandy/jingwei-base/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-zxgangandy-orange.svg)](https://github.com/zxgangandy/base-eureka)
[![](https://img.shields.io/badge/version-1.0.6-brightgreen.svg)](https://github.com/zxgangandy/base-eureka)

### usage
- 添加依赖：

```xml
 <dependency>
  <groupId>io.github.zxgangandy</groupId>
  <artifactId>base-eureka</artifactId>
  <version>1.0.7</version>
 </dependency>
```

- 添加注解
@EnableEurekaManage

- 补充说明
    如果希望上线、下线操作能够快速生效，还需要做以下额外配置：
    以下配置的时间越小，则生效时间越快，任何一个较大都可能影响生效时间（以下设置为生产环境推荐值，并不是原框架的默认值）

    **服务注册中心**<br>
    eureka.server.response-cache-update-interval-ms=1000 // 注册中心刷新响应缓存的频率，单位ms
    
    **服务调用方**<br>
    eureka.client.registry-fetch-interval-seconds=5 //客户端从注册中心拉取实例信息的频率，单位为s
    xx-service.ribbon.ServerListRefreshInterval=1000 //ribbon刷新本地缓存的频率，单位ms，前缀为服务提供方的spring.application.name：xx-service
    
- service online <br>
    api path : /manage/online 

- service offline <br>
    api path: /manage/offline 
