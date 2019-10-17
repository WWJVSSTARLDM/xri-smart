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