# 验证码组件的使用简介

## 功能介绍
1. 提供数字、字母组合的验证码及图片，支持常规的**登录+验证码**场景。

2. 提供阿里云短信验证码（6位随机数字）服务，支持**手机号+验证码**注册，**手机号+验证码**登录的应用场景。

3. 支持单节点部署和集群部署模式，集群模式目前只支持Hazelcast集群模式。

## 配置介绍

**首先添加Maven的组件依赖**  

```java
<dependency>
    <groupId>com.light</groupId>
    <artifactId>framework-kaptcha</artifactId>
</dependency>
```

**单节点部署**
基于Session存储客户端信息，YML配置文件中添加如下配置

```yaml
framework:
  kaptcha: 
    resolver: session
    sms: # 阿里云短信验证码服务配置
      aliyun: 
        enabled: true
        accessKeyId: xxxxx #阿里云短信服务的 accessKeyId
        accessSecret: xxxx #阿里云短信服务的 accessSecret
        signName: xxxx     #阿里云短信服务的 短信签名

```

**集群部署** 基于Hazelcast集群，需要先配置Hazelcast实例，具体配置可参考官网，以下提供最简单的基于局域网广播模式的集群服务发现配置，以供参考

（1） Hazelcast缓存集群配置

```java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.light.kaptcha.impl.HazelcastKaptchaResolver;

@Configuration
public class HazelcastConfig {

	@Bean(destroyMethod = "shutdown")
	public HazelcastInstance hazelcastInstance() {
		Config config = new Config();
		config.getMapConfig("accessTokens").setTimeToLiveSeconds(3600 * 24 * 30);
		
		// 配置验证码缓存策略，缓存生命周期配置为5分钟
		HazelcastKaptchaResolver.configCache(config, 300*1000L);
		
		return Hazelcast.newHazelcastInstance(config);                  
	}
}

```

（2） YML配置文件resolver配置为Hazelcast

```yaml
framework:
  kaptcha: 
    resolver: hazelcast
    sms: # 阿里云短信验证码服务配置
      aliyun: 
        enabled: true
        accessKeyId: xxxxx #阿里云短信服务的 accessKeyId
        accessSecret: xxxx #阿里云短信服务的 accessSecret
        signName: xxxx     #阿里云短信服务的 短信签名

```

## 使用介绍
**登录+验证码** 应用场景  

1. 访问 **/kaptcha** 接口获取验证码图片  
2. 验证码的校验，支持注解和编码方式  


- 注解方式：支持Spring的Validator框架，只需要在DTO中的验证码字段上添加 @Kaptcha 注解即可，举个栗子    

```java
@Data
public class CaptchaDTO {

	private String username;
	
	private String password;
	
	@Kaptcha
	private String captcha;
}
```

- 编码方式：调用KaptchaService的validCaptcha接口，参数需要根据cookie解析，较为复杂，就不做介绍了

```java
    KaptchaService.instance().validCaptcha(String indentityKey, String scope, String captchaText)

```

**手机号+验证码** 注册、登录的应用场景

（1）发送验证码（阿里云短信服务使用请参考阿里官网）  

```java
	@Autowired
	private SmsCaptchaService smsCaptchaService;
	/** 阿里云短信服务的 短信模板编码 */
	private final String SMS_TEMPLATE_CODE = "SMS_163XXXXXX";
	
	public void sendSmsCaptcha(String mobile) {
	    String scope = "register"; //用户注册
	    Response rs = smsCaptchaService.sendSmsCaptcha(mobile, scope, SMS_TEMPLATE_CODE);
	}
	

```
（2）验证码的校验，支持注解和编码方式 

- 注解方式：DTO实现SmsCaptchaGetter接口，添加注解@SmsKaptcha  

```java
@Getter@Setter
@SmsKaptcha(scope="register")
public class UserRegisterDTO implements SmsCaptchaGetter {

	private String mobile;
	private String captcha;
	private String password;
}
```

- 编码方式：调用KaptchaService的validCaptcha接口验证  

```java
    public void validCaptcha(String mobile, String scope, String captcha) {
        boolean isValid = KaptchaService.instance().validCaptcha(mobile, scope, captcha);
    }

```