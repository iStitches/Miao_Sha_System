# 秒杀系统

> 模拟高并发情况下的秒杀，保证不会出现长时间堵塞、超卖的情况。重点在于如何对流量削峰、如何保证缓存和数据库数据一致性。

本项目重点在于编写后端代码，前端界面之后会补充上来。

## 安装和部署

### 必要软件

* Java编程环境、编程工具IDEA
* 数据库 MySQL
* 缓存中间件 Redis
* 消息中间件 RabbitMQ
* 用户身份认证、状态保存 Jwt

### 配置和运行

* 更改主配置文件 `application.yml`，更换里面的数据源配置、缓存配置、消息中间件配置
* 配置完成后即可直接运行项目

## 项目流程

![image-20210601183243578](passageImg/image-20210601183243578.png)

## 项目特色

* 结合 jwt、token、redis 实现无状态请求下的用户身份认证和信息保存操作。由于HTTP为无状态的请求协议，因此同一个用户多个页面间的切换每次都需要进行身份认证操作，非常不方便。采用 jwt、token的方式使得服务端不需要保存每个用户的身份信息，真正的身份信息在第一次登录成功后就返回`accesstoken`给了客户端，客户端每次请求时都会带上这个`accesstoken`，服务端只需要对`accesstoken`进行解析验证即可。
* Redis 缓存的使用，一方面用来保存登录成功后的用户身份信息，方便后续与用户信息相关操作的处理；

另一方面用来缓存一些静态页面、预减库存，来达到减少访问数据库的次数，减少数据库压力。

* RabbitMQ消息队列的使用，一方面用在大流量请求时对接收请求和处理请求两个操作进行异步处理，降低了同一时间点数据库的压力；另一方面用在数据更新前后的延迟双删处理。



