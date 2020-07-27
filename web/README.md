## 通用的 WEB 依赖包

### 应用场景描述

- 需要快速构建一个Web工程, 提供最基本的Rest服务
- 要支持必要的服务监控  
- 方便配置服务度量指标和实时获取度量值
- 支持本地和分布式缓存
- 支持本地和分布式日志管理
- 自动提供动态的API接口文档

### 功能描述
通用的Web依赖包, 结合了很多流行的微服务开源框架  

- 集成SpringMVC, 对外提供最基本的Rest服务
- 集成Swagger, 能够支持为Restful接口生成动态的接口文档, 减少项目接口文档的维护成本
- 集成Metrics, 一个通用的度量统计框架, 可以方便的对业务代码的各个指标进行监控

以下为工程提供的共通

1. 接口返回结构的封装; Convertor基类的实现, 提供Model与DTO转换的通用方法; 通用的Bean定义(比如键值对结构)
2. Request请求参数自定义参数类型封装. 比如 Searchable
3. 异常的统一处理和国际化. Controller层面拦截所有抛出的异常, 国际化异常信息, 以统一的格式输出给客户端
4. 提供常用的工具类


### 快速使用手册
- 添加Mave工程的依赖到pom.xml

```xml
    <parent>
		<groupId>com.light</groupId>
		<artifactId>parent</artifactId>
		<version>2.1.6-SNAPSHOT</version>
	</parent>
	
	<dependencies>
		<dependency>
			<groupId>com.light</groupId>
			<artifactId>web</artifactId>
		</dependency>
		
		...
	</dependencies>
```

- 标准的spring boot启动方式, 参考代码如下:

```
@SpringBootApplication
public class WebApplication {

    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved
     *         into an address
     */
    public static void main(final String[] args) throws UnknownHostException {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

- 配置生成容器中可运行的war包. 添加SpringBootServletInitializer实现类, 参考下面的示例:

```java
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }
}
```

- 核心的配置项 (在src/main/resources/application.yml中配置)
spring-boot的通用配置参考官方文档  http://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/common-application-properties.html

```
light: 
  web: 
    async: # 异步线程池配置, 默认生效
      corePoolSize: 2 # the number of threads to keep in the pool, even if they are idle 默认值 2
      maxPoolSize: 50 # the maximum number of threads to allow in the pool 默认值 50
      queueCapacity: # the queue size to use for holding tasks before they are executed 默认值 10000
    #cors: # 跨域访问配置, 默认不生效. 取消注释才能生效.
      #allowed-origins: "*"
      #allowed-methods: GET, PUT, POST, DELETE, OPTIONS
      #allowed-headers: "*"
      #exposed-headers:
      #allow-credentials: true
      #max-age: 1800
    swagger: # Swagger动态接口文档配置
      title: WebMVC API # 文档标题
      description: WebSample API documentation # 文档描述
      version: 0.0.1 # API版本号
      termsOfServiceUrl: # 服务条款访问地址
      contactName: # 联系人
      contactUrl:  # 联系地址
      contactEmail: # 联系邮箱
      license:
      licenseUrl: 
      pathPatterns: # API 路径匹配表达式, 列表方式
        - /**
        #- /web/**
        #- /mobile/**
```