package com.ice.eclair.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 22:56
 * @Description:
 */
@Configuration
public class SessionFactoryInit {
	@Bean
	public DbConfig dbConfig() {
		return new DbConfig();
	}

	@Bean
	public SessionFactory sessionFactory() {
		return new SessionFactory(dbConfig());
	}

}
