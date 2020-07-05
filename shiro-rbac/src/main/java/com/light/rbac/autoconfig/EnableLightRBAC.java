package com.light.rbac.autoconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.light.rbac.RbacConfiguration;

/**
 * 启用 基于资源的权限框架
 *
 * @author liuyg
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {RbacConfiguration.class })
@Documented
public @interface EnableLightRBAC {
}
