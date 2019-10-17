## Spring Cloud

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

##### 5.6.6 Yml 依赖配置
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