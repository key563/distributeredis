
## actuator 配置说明

### 作用
主要是完成微服务的监控，完成监控治理。可以查看微服务间的数据处理和调用，当它们之间出现了异常，就可以快速定位到出现问题的地方。

### 依赖
```xml
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 配置
SpringBoot2.x相对于1.5.x版本而言，Actuator也发生很多变化，配置也发生了相应的变化。

#### 基本配置
```properties
# 端口号
management.server.port=9001
# API路径
management.server.servlet.context-path=/
# health是否显示细节，可选never,always,when-authenticated
management.endpoint.health.show-details=always
# 公开所有端点，默认只有端点/health和/info端点是暴露的，可以通过include和exclude进行包括和排除
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env
```
#### 1.5.x与2.x版本属性比较
旧的属性 | 新的属性 |
:---: | :---: | 
endpoints.<id>.*	|   management.endpoint.<id>.*
endpoints.cors.*	|   management.endpoints.web.cors.*
endpoints.jmx.*	    |   management.endpoints.jmx.*
management.address  |	management.server.address
management.context-path |	management.server.servlet.context-path
management.ssl.*    |	management.server.ssl.*
management.port     |	management.server.port


>说明：
SpringBoot2.x已经去掉了management.security的配置

### endpoints接口信息

* **所有 endpoints 默认情况下都已移至"/actuator"。即多了根路径actuator**
* **默认只有端点/health和/info端点是暴露的。**

#### endpoints属性
HTTP 方法| 路径 | 描述
:---: | :---: | :---: |
GET	|	**/actuator/conditions**	|	提供了一份自动配置报告，记录哪些自动配置条件通过了，哪些没通过（之前为autoconfig）
GET	|	/actuator/configprops	|	描述配置属性(包含默认值)如何注入Bean
GET	|	/actuator/beans	|	描述应用程序上下文里全部的Bean，以及它们的关系
GET	|	/actuator/dump	|	获取线程活动的快照
GET	|	/actuator/env	|	获取全部环境属性
GET	|	/actuator/env/{name}	|	根据名称获取特定的环境属性值
GET	|	/actuator/health	|	报告应用程序的健康指标，这些值由HealthIndicator的实现类提供
GET	|	/actuator/info	|	获取应用程序的定制信息，这些信息由info打头的属性提供
GET	|	/actuator/mappings	|	描述全部的URI路径，以及它们和控制器(包含Actuator端点)的映射关系
GET	|	/actuator/metrics	|	报告各种应用程序度量信息，比如内存用量和HTTP请求计数
GET	|	/actuator/metrics/{name}	|	报告指定名称的应用程序度量值
POST|   /actuator/shutdown	|	关闭应用程序，要求endpoints.shutdown.enabled设置为true
GET	|	/actuator/httptrace	|	提供基本的HTTP请求跟踪信息(时间戳、HTTP头等)（之前为trace）

