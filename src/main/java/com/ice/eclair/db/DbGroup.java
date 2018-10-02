package com.ice.eclair.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 21:56
 * @Description: 每一个数据库连接
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbGroup {
	static final String defalutGroup = "default";
	/**
	 * @desc: 主库还是读库
	 */
	private String name;
	/**
	 * @desc: 每个库下的连接属性
	 */
	private List<DbProp> infoList;
}
