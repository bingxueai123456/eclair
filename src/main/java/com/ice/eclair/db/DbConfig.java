package com.ice.eclair.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 21:54
 * @Description: 数据库的配置类
 */
@ConfigurationProperties("db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DbConfig {
	private List<String> dbMappers;
	private List<DbGroup> dbGroups;

}
