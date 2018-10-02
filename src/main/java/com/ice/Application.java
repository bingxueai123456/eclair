package com.ice;

import com.ice.eclair.db.EnableSessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 23:13
 * @Description:
 */
@SpringBootApplication
@EnableSessionFactory
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
