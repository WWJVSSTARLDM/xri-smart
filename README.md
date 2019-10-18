## Spring Cloud 微服务框架模板

### 搭建环境：

| 工具         | 版本               |
| ------------ | ----------------- |
| JDK          | 1.8               |
| Maven        | 3.6.1             |
| IDE          | IntelliJ IDEA     |
| Spring Boot  | 2.1.6.RELEASE     |
| Spring Cloud | Greenwich.SR1     |



### 关于我：

- 本人联系QQ：200538725 微信：Xri2117，若发现问题或者有问题请教可以随时加我，主要目的为了准备学习SpringCloud的朋友一些帮助。
- 后续会更新其他得 如您觉得该项目对您有用，欢迎点击右上方的Star按钮，给予支持！！

- 温馨提示: 本教程使用聚合工程，父工程pom.xml请看源码，每个组件只会标注需要引入的依赖，父工程进行版本管理。

### 模块介绍

| 服务名                          | 简介                        |
| ------------------------------- | --------------------       |
| xri-common                      | 公共模块,公用实体类对象      |
| xri-eureka                      | Eureka 注册中心服务端        |
| xri-config-server               | Config配置中心,集中配置      |
| xri-gateway                     | GateWay网关                |
| xri-service-api                 | 对外提供接口Api(父模块)     |
| xri-service-api-provider        | 具体Api接口-提供者接口(子模块)|
| xri-hystrix-dashboard           | hystirx监控中心             |
| xri-service-modules             | 具体业务模块(父模块)         |
| xri-service-api-login           | 具体业务模块-登录模块(父模块) |
| xri-service-api-consumer-impl   | 具体业务模块-消费者模块(子模块)|
| xri-service-api-provider-impl   | 具体业务模块-提供者模块(子模块)|

### 内容介绍

| 序号   | 组件        | 简介           |
| ---- | --------- | ------------ |
| 一    | Eureka    | 注册中心         |
| 二    | Client    | 微服务客户端模板     |
| 三    | Ribbon    | 负载均衡         |
| 四    | Feign     | 负载均衡         |
| 五    | Hystrix   | 断路器          |
| 六    | Dashboard | Hystrix仪表盘组件 |
| 七    | GateWay      | 服务网关         |
| 八    | Config    | 配置中心         |
| 九    | Bus       | 消息总线         |
| 九    | Turbine    | Hystrix 集群监控|
| 十    | Zipkin    | 微服务链路跟踪|


### 一、Eureka 注册中心 (重点使用)

#### 1.1 简介

​	**Eureka是Spring Cloud Netflix微服务套件中的一部分，可以与Springboot构建的微服务很容易的整合起来。Eureka包含了服务器端和客户端组件。服务器端，也被称作是服务注册中心，用于提供服务的注册与发现。**	

​	 **Eureka支持高可用的配置，当集群中有分片出现故障时，Eureka就会转入自动保护模式，它允许分片故障期间继续提供服务的发现和注册，当故障分片恢复正常时，集群中其他分片会把他们的状态再次同步回来。客户端组件包含服务消费者与服务生产者。在应用程序运行时，Eureka客户端向注册中心注册自身提供的服务并周期性的发送心跳来更新它的服务租约。同时也可以从服务端查询当前注册的服务信息并把他们缓存到本地并周期性的刷新服务状态。**

#### 1.2 Pom 配置

```xml
        <!--  eureka 服务端  -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
```

#### 1.3 启动类 配置

```java

/**
 * @Author : Crazy.X
 * @Date : 2019/10/9
 *
 * Eureka 服务端
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApp {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp.class, args);
    }
}

```
#### 1.4 Yml 配置

```yaml
eureka:
  client:
    service-url:
      # 注册中心地址
      defaultZone: http://localhost:8761/eureka/
      # Eureka高可用，如果有多个Eureka，将采用互相注册法，若是2个以上Eureka 使用','分割
      # 两个 Eureka 互相注册
      # 8761 注册到 8762
      # Eureka 8761 ：defaultZone: http://localhost:8762/eureka/
      # 8762 注册到 8761
      # Eureka 8762 ：defaultZone: http://localhost:8761/eureka/
      # 多个Eureka 以','分割  8761 8762 8763 以8762为例，以此类推
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8763/eureka/
    # 禁止eureka向自己注册
    register-with-eureka: false
    fetchRegistry: false
  # 关闭eureka心跳警告(不建议):
  server:
    enable-self-preservation: false
    # eureka server刷新readCacheMap的时间，注意，client读取的是readCacheMap
    # 这个时间决定了多久会把readWriteCacheMap的缓存更新到readCacheMap上
    response-cache-update-interval-ms: 30000
    # 启用主动失效，并且每次主动失效检测间隔为30s，默认为0
    eviction-interval-timer-in-ms: 30000
# eureka 应用名称
spring:
  application:
    name: xri-smart-eureka
#tomcat端口（8761也是默认端口）
server:
  port: 8761
```
### 二、Client 客户端（所有Client模板）

#### 2.1 Pom 配置
```xml
        <!--  Spring boot web 模块-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--  Spring boot client 客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
#### 2.2 启动类 配置
```java

/**
 * 表示该服务是Client客户端，并向Eureka注册
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}

```
#### 2.3 Yml 配置
```yaml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
# client 应用名称
spring:
  application:
    name: xri-service-api-provider-impl
#tomcat端口
server:
  port: 8001
```

### 三、Ribbon 负载均衡

### 3.1 简介

​	**Spring Cloud Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，它基于Netflix Ribbon实现。通过Spring Cloud的封装，可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用。Spring Cloud Ribbon虽然只是一个工具类框架，它不像服务注册中心、配置中心、API网关那样需要独立部署，但是它几乎存在于每一个Spring Cloud构建的微服务和基础设施中。因为微服务间的调用，API网关的请求转发等内容，实际上都是通过Ribbon来实现的，包括后续我们将要介绍的Feign，它也是基于Ribbon实现的工具。所以，对Spring Cloud Ribbon的理解和使用，对于我们使用Spring Cloud来构建微服务非常重要。**

#### 3.2 Bean 配置

```java
@Configuration
public class CloudConfig {

    /**
     * Ribbon是基础Netflix Ribbon实现的一套客户端 负载均衡工具
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```
#### 3.3 Eureka 服务
- 服务端若是集群，默认是轮询调用


#### 3.4 调用 配置 
```java
@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 注册到Eureka的 http:// + 服务名称 + 控制器
     * @return
     */
    @GetMapping("/test")
    public String hello() {
        return restTemplate.getForObject("http://SPRING-CLOUD-CONSUMER/hello", String.class, String.class);
    }

}
```
### 四、Feign 负载均衡（重点使用）

#### 4.1 简介

​	**Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。**

**简而言之：**

- **Feign 采用的是基于接口的注解**

- **Feign 整合了ribbon，具有负载均衡的能力**

- **整合了Hystrix，具有熔断的能力**

#### 4.2 Pom 配置
```xml
        <!-- openfeign 组件 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```
#### 4.3 Provider 服务提供者(采用多模块)


##### 4.3.1 Provider 启动类

```java
/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
```
##### 4.3.2 Feign 接口
```java
/**
 * name调用哪个服务的那个接口(注册到Eureka的服务)
 * 由于接口是提供方提供，这里选择继承提供方的接口并声明@FeignClinet
 */
@FeignClient(name = "xri-service-api-provider-impl")
public interface OrderServiceFeign extends OrderService {
  
}

/**
 * 提供方提供的接口
 * 接口需要对应Controller的controller注解，参数注解以及参数必须一致；
 */
public interface OrderService {
    @GetMapping("/order")
    Order getOrder(@RequestParam Long id);
}

/**
 * 提供方自己对接口的实现，给提消费者提供远程调用实现
 */
@Service
public class ProviderServiceImpl implements OrderService {

    @Override
    public Order getOrder(Long id) {
        Order order = new Order();
        order.setOrderId(post);
        order.setOrderName("兰博基尼");
        return order;
    }
}
```
##### 4.3.3 Prodvider Feign接口对应的Controller
```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ProviderController {

    @Autowired
    private OrderService orderServiceImpl;

    @GetMapping("/order")
    public Order getOrder(@RequestParam Long id) {
        System.out.println("ProviderController......");
        return orderServiceImpl.getOrder(id);
    }
}
```
#### 4.4 Consumer 服务消费者

> 消费者进行调用提供者的接口

##### 4.4.1 Consumer 启动类
```java
/**
 * 消费者启动类，启用Feign
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
```
##### 4.4.2 注入接口进行调用
```java
/**
 * OrderServiceFeign 通过Feign进行服务之间的调用
 */
@Service
public class ConsumerService {

    @Autowired
    OrderServiceFeign orderServiceFeign;

    public Order getOrder(Long id) {
        Order order = orderServiceFeign.getOrder(id);
        return order;
    }
}
```
### 五、Hystrix 断路器

#### 5.1 简介
​	**在微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以相互调用（RPC），在Spring Cloud可以用RestTemplate+Ribbon和Feign来调用。为了保证其高可用，单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证100%可用，如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。**

#### 5.2 Pom 配置
```xml
        <!-- hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
#### 5.3 启动类 配置
```java
/**
 * 开启Hystrix
 * 
 * @SpringCloudApplication 是组合注解相当于下面三个注解一起使用,这里只说明一次
 * @EnableDiscoveryClient
 * @EnableCircuitBreaker
 * @SpringBootApplication
 */
@EnableFeignClients
@SpringCloudApplication
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
```
#### 5.4 Yml 配置
```yaml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
# client 应用名称
spring:
  application:
    name: xri-service-api-provider-impl
#tomcat端口
server:
  port: 8001
```

#### 5.5 使用Hystrix 服务熔断
```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ConsumerController {

    @Autowired
    ConsumerService providerService;

    /**
     * 弱抛出异常，将会引发Hystrix机制，fallback方法
     * @HystrixCommand 开启熔断 fallbackMethod:指定回调方法
     */
    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/order")
    public Order getOrder(@RequestParam Long id) {
        System.out.println("ConsumerController...");
        return consumerService.getOrder(id);
    }


    public Order fallback(@RequestParam Long id) {
        System.out.println("hystrix 介入..");
        return new Order("hystrix", "123456");
    }
}
```
#### 5.6 使用Hystrix 服务降级 消费者（重点使用）
> **使用场景：类似天猫双11，并发量高会关掉某些查询数据库的服务进行降级，返回一个预处理结果**
> **提供者、消费者都要引入相同依赖**

##### 5.6.1 Pom 配置
```xml
        <!-- Hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
##### 5.6.2 消费者启动类 配置
```java

@EnableFeignClients
@SpringCloudApplication
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}

```
##### 5.6.3 消费者使用Feign 接口通信

```java
/**
 * Feign组件
 * name     Eureka注册的要调取的服务
 * fallback 出现服务中断，则调取降级方法
 * 此处一样继承提供方提供的Api接口，后面不再重复
 */
@FeignClient(name = "xri-service-api-provider-impl", fallback = OrderServiceFallback.class)
public interface OrderServiceFeign extends OrderService {
}

```
##### 5.6.4 Feign对应的Controller

```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ConsumerController {

    @Autowired
    ConsumerService providerService;

    @GetMapping("/order")
    public Order getOrder(@RequestParam Long id) {
        System.out.println("ConsumerController...");
        return consumerService.getOrder(id);
    }
}
```
##### 5.6.5 Hystrix 回退类
```java
/**
 * Hystrix 回退类
 * Provider服务若中断则进行服务降级
 * 要求实现实现了Feign的接口
** OrderServiceFeign 接口则是自己实现的接口并且继承提供方提供的Api接口
 */
@Component
@Slf4j
public class OrderServiceFallback implements OrderServiceFeign {
    private static Order order;

    static {
        order = new Order(500L, "系统异常，服务降级");
    }

    @Override
    public Order getOrder(Long id) {
        log.error("系统异常,请及时处理");
        return order;
    }
}
```

##### 5.6.6 Yml 
```yaml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
# client 应用名称
spring:
  application:
    name: xri-service-api-provider-impl
#tomcat端口
server:
  port: 8001
# 开启hystrix 熔断降级（这里如果不配置，将无效）
feign:
  hystrix:
    enabled: true
```

### 六、HystrixDashboard 服务监控

### 6.1 简介

​	**Hystrix Dashboard是Hystrix的仪表盘组件，主要用来实时监控Hystrix的各项指标信息，通过界面反馈的信息可以快速发现系统中存在的问题。**

​	**使用Hystrixdashboard 必须在提供者提供的接口上使用@HystrixCommand注解,可以不提供熔断函数，但是不提供也将出现异常无回调函数，导致系统出现雪崩。**

#### 6.2 Pom 配置
```xml
        <!-- eureka 客户端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- hystrix 组件-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!-- hystrix-dashboard -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <!-- actuator 组件 健康检查、审计、统计和监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

#### 6.3 启动类 配置
```java
/**
 * 开启EnableHystrixDashboard
 */
@EnableHystrixDashboard
@SpringBootApplication
public class HystrixDashboardApp {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApp.class, args);
    }

}

```

#### 6.3.1 Hystrixdashborard Yml配置
```yml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
# client 应用名称
spring:
  application:
    name: xri-hystrix-dashboard
#tomcat端口
server:
  port: 8762
```


#### 6.4 需要监控的服务 （消费者端）

##### 6.4.1 Pom 配置
> 所有需要被监控的微服务都要引入该依赖(必须)

```xml
        <!-- actuator 组件 健康检查、审计、统计和监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

##### 6.4.2 启动类配置 
```java

/**
 * Consumer启动类，并且开启hystrix
 */
@EnableFeignClients
@SpringCloudApplication
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
```


##### 6.4.3 Yml 配置
```yaml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
# client 应用名称
spring:
  application:
    name: xri-service-api-consumer-impl
#tomcat端口
server:
  port: 8001
# 开启hystrix 熔断降级（这里如果不配置，将无效）
feign:
  hystrix:
    enabled: true
# 使用服务监控释放的访问路径,后续的配置中心bus刷新也需要释放
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

##### 6.4.4 消费者 接口

```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ConsumerController {

    @Autowired
    ConsumerService providerService;

    @GetMapping("/order")
    public Order getOrder(@RequestParam Long id) {
        System.out.println("ConsumerController...");
        return consumerService.getOrder(id);
    }
}
```
##### 6.4.5 监控步骤
###### 6.4.5.1 启动HystrixDashdoard服务

> 运行Hystrix微服务 访问  `http://localhost:8762/hystrix` 会出现Hystrix页面，也就代表服务运行成功


###### 6.4.5.2 使用监控
 - 在路径填写要被监控的服务即可 根据业务填写具体端口
    `http://localhost:8501/actuator/hystrix.stream`
 - Delay：2000
 - Title：标题


### 七、Gateway 网关

### 7.1 简介

​	**Spring Cloud Gateway是Spring官方基于Spring 5.0，Spring Boot 2.0和Project Reactor等技术开发的网关，Spring Cloud Gateway旨在为微服务架构提供一种简单而有效的统一的API路由管理方式。Spring Cloud Gateway作为Spring Cloud生态系统中的网关，目标是替代Netflix ZUUL，其不仅提供统一的路由方式，并且基于Filter链的方式提供了网关基本的功能，例如：安全，监控/埋点，和限流等。**


#### 7.2 Pom 配置
```xml
        <!-- Eureka 客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- gateway 网关-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!-- hystrix 服务降级 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
#### 7.3 启动类 配置
```java
/**
 * 开启Gateway网关
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApp {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApp.class,args);
    }
}
```
#### 7.4 Yml 配置
```yaml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
# client 应用名称
spring:
  application:
    name: xri-gateway
  cloud:
    gateway:
      discovery:
        locator:
          # 开启此配置无需配置路由即可根据Eureka注册服务名转发 false暂时是不开启
          enabled: false
          # 开启url路径服务名使用小写(开启后无法使用大写)
          # localhost:9000/scm-service-consumer/order
          lower-case-service-id: true
#tomcat端口
server:
  port: 9000
```
#### 7.5 网关配置路由
- 网关路由是可以通过yml配置也可以直接代码配置，逐一介绍

##### 7.5.1 Config 路由配置(代码版本)
```java
 * Gateway 路由配置
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        /**
         * 详情配置参照官方文档
         * {@link} https://github.com/spring-cloud-samples/spring-cloud-gateway-sample/blob/master/src/main/java/com/example/demogateway/DemogatewayApplication.java
         */
        return builder.routes()
                /**
                 * consumer 是本次consumer的id
                 * r.path("/consumer/**")是请求的url
                 * .uri("http://www.jd.com")则是进行转发到京东
                 */
                .route("consumer", r -> r.path("/consumer/**")
                        .uri("http://www.jd.com"))
                .route("scm-service-consumer", r -> r.path("/api/**")
                        /**
                         * 整合 Hystrix 加入回滚
                         * f.stripPrefix(1) 去掉第一个前缀转发 没有了api
                         * localhost:9000/api/order?id=1 -> localhost:9000/xr-service-api-consumer-impl/order?id=1
                         * url就是转发路径。lb是轮训机制到该服务下面的所有节点
                         */
                        .filters(f -> f.stripPrefix(1).hystrix(config -> config.setFallbackUri("forward:/hystrixFallback")))
                        .uri("lb://XRI-SERVICE-API-CONSUMER-IMPL"))
                .build();
    }
}
```
##### 7.5.2 不使用 gateway 访问
```yaml
#　直接请求服务的Url 9000是网关的端口
http://localhost:9000/api/order?id=1
```

##### 7.5.3 使用 gateway 访问
```yaml
#　需要根据route设置加入前缀进行网关转发
localhost:9000/api/order?id=1
```

##### 7.5.4 路由访问映射Yml版本
> - yml 配置

```yml
  待补充
```

### 八、Config 配置中心

### 8.1 简介

​	**Spring Cloud Config为分布式系统中的外部配置提供服务器和客户端支持。使用Config Server，您可以为所有环境中的应用程序管理其外部属性。它非常适合spring应用，也可以使用在其他语言的应用上。随着应用程序通过从开发到测试和生产的部署流程，您可以管理这些环境之间的配置，并确定应用程序具有迁移时需要运行的一切。服务器存储后端的默认实现使用git，因此它轻松支持标签版本的配置环境，以及可以访问用于管理内容的各种工具。**

#### 8.2 Pom 配置 
```xml
        <!-- SpringCloud Config server端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <!-- Eureka 客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
#### 8.3 启动类 配置
```java
/**
 * 声明该服务 是cloud config server
 */
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class, args);
    }
}
```
#### 8.4 yml 配置
```yaml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: xri-config-server
    # Github远程仓库地址
  cloud:
    config:
      server:
        git:
          # git 存放配置文件项目地址
          uri: https://github.com/xr2117/xri-smart-config.git
          username: github账号
          password: github密码
          # GitHub如果创建文件夹，则指定在那个文件夹下
#          search-paths: /dev
#tomcat端口
server:
  port: 3344
```
#### 8.5 Github 创建远程仓库

- 创建存放配置文件仓库

- Github创建仓库这里不做讲解，
`https://www.cnblogs.com/zhixi/p/9584624.html` 可根据介绍创建

##### 8.5.1 创建 Yml push到Github
**下面以消费者为例，由于一些公用的配置，比如Eureka注册是每个注册到Eureka服务都需要的配置，这里可以提取为公用配置文件，在自己服务的基础上需要各自的配置在进行创建单独的排位置文件,微服务拉去配置会先读取application.yml，因为application.yml优先级最高。
文件也是区分dev模式和master模式，也就是所谓的测试版本和上线版本**

> 公用配置文件新建yml文件（文件名application-dev，重点：格式一定为UTF-8）
```yml
eureka:
  instance:
    # 现实服务的IP:Port
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    # 不加此项 如果注册中心 和 服务位于同一服务器，会导致 注册的ip为 localhost，导致其他 地址 无法访问此 服务
    prefer-ip-address: true
    ip-address: ${spring.cloud.client.ip-address}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

> - 1. 新建yml文件（文件名xri-service-api-consumer-impl-dev，重点：格式一定为UTF-8）
```yaml
spring:
  application:
    # 注册到Eureka服务的应用名称
    name: xri-service-api-consumer-impl
  datasource:
    # 本次为介绍，加入hikari 连接池
    hikari:
      # 详细配置请访问：https://github.com/brettwooldridge/HikariCP
      data-source-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://106.13.90.174:3306/test
      username: root
      password: root
      # 最小空闲链接数量
      minimum-idle: 5
      # 空闲连接存活最大时间，默认600000（10分钟）
      idle-timeout: 180000
      # 连接池最大连接数，默认是10
      maximum-pool-size: 8
      # 数据库连接超时时间,默认30秒，即30000
      connection-timeout: 30000
# 开启服务降级
feign:
  hystrix:
    enabled: true
```

> - 2. push 到github 远程仓库

**这里不做git命令教学，请自己学习**

### 8.6 搭建config clinet端（以consumer为例）

#### 8.6.1 Pom 配置
```xml
        <!-- SpringCloud Config 客户端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-client</artifactId>
        </dependency>
```
#### 8.6.2 启动类 配置
```java
@EnableFeignClients
@SpringCloudApplication
public class ConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}

```
#### 8.6.3 (bootstrap.yml) 配置

- 使用Spring Cloud 配置中心，配置文件名字为 bootstrap.yml (重点：命名规范)

```yaml
spring:
  application:
    # 应用名称并且对应github文件名前缀（xri-service-api-consumer-impl-dev.yml）
    name: xri-service-api-consumer-impl
  cloud:
    config:
      discovery:
        # 开启cloud配置
        enabled: true
        # 注册到Eureka的服务（config server的注册名）
        service-id: xri-config-server
      # 本次访问的配置项(即是xri-service-api-consumer-impl-dev配置，可切换master)
      profile: dev
      # 若拉取不到配置文件则不适用默认配置，即8080Tomcat默认
      fail-fast: true
# 由于微服务会搭建集群，实例为同一个服务，但是端口不同，所以端口进行再项目里的bootstrap.yml 进行配置
server:
  port: 8501
```
### 8.7 运行测试 + 运行流程介绍
> clinet端通过yml配置找到对应注册中心的server服务，server服务配置好了github仓库进行拉取配置，clinet读取server拉取下来的配置进行启动
>
```java
@RestController
public class ConsumerController {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @RequestMapping("/config")
    public void getConfig() {
        System.out.println("applicationName : " + applicationName);
    }
}

