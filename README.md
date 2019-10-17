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