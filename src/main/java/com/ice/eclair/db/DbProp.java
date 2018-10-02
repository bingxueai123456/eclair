package com.ice.eclair.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 22:01
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbProp {
	private String host;
	private String name;
	private Integer port;
	private String password;
	private String user;
	private String environment;
	private Integer poolMaximumActiveConnections;
	private Integer poolMaximumIdleConnections;

	private static final AtomicInteger idx = new AtomicInteger(0);

	public String getEnvironment() {
		if (Objects.isNull(environment)) {
			environment = "dev-" + idx.incrementAndGet();
		}
		return environment;
	}

	public Integer getPort() {
		if (Objects.isNull(port)) {
			return 3306;
		}
		return port;
	}

}
