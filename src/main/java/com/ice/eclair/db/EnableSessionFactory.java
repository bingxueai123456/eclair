package com.ice.eclair.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 22:56
 * @Description:
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@Import(SessionFactoryInit.class)
@Configuration
public @interface EnableSessionFactory {
}

