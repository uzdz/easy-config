# easy-config 简单的配置刷新工具
> 采用Zookeeper Watch机制与Spring ApplicationListener搭配实现的属性动态刷新插件。

# 使用文档

## 第一步: 添加依赖
> pom.xml中添加如下依赖

```xml
<dependency>
    <groupId>com.uzdz.group</groupId>
    <artifactId>config</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
## 第二步: 启动配置中心刷新组件
> 在核心启动类Application里添加@EnableEasyConfig(startup = true)注解

> 当startup为false时不开启自动配置刷新

```java
@SpringBootApplication
@EnableDubbo
@EnableEasyConfig(startup = true)
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
    }
}
```

## 第三步: 在application.yml添加必须参数
> 声明配置注册中心zookeeper地址、服务名称

```yaml
config:
  zookeeper:
    address: localhost:2181# 必须参数（注册地址）
  server:
    name: gateway# 必须参数（服务名称）
```

## 第四步: 在需要刷新的类上加入@RefreshClass注解
> 声明该类下的字段被作用于属性刷新

```java
@RefreshClass
public class GatewayMainFilter implements Filter {
    
}
```

## 第五步: 在需要刷新的字段上加入@RefreshField注解
> 声明该字段被作用于属性刷新，key用来标注键，propKey表示当获取失败或无Node节点时从配置文件获取该key的value值作为初始化的默认值

```java
@RefreshClass
public class GatewayMainFilter implements Filter {

    @RefreshField(key = "env", propKey = "gateway.env")
    public String env = null;
    
}
```