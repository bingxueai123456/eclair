package com.ice.eclair.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 22:58
 * @Description:
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Slf4j
public class SessionFactory {

	private static class GroupInfo {
		AtomicLong seq;
		List<SqlSessionFactory> factories;

		GroupInfo(List<SqlSessionFactory> factories) {
			this.seq = new AtomicLong(0);
			this.factories = factories;
		}

	}

	private Object sessionGroupsLock = new Object();
	private Map<String, GroupInfo> sessionGroups;
	private Map<String, DbGroup> dbGroupConfigs;
	private Set<String> mappers;

	public SessionFactory(DbConfig dbConfig) {
		this.dbGroupConfigs = new HashMap<>();
		this.sessionGroups = new HashMap<>();
		this.mappers = new HashSet<>();
		this.initConfig(dbConfig);
	}

	private void initConfig(DbConfig dbConfig) {
		if (!Objects.isNull(dbConfig)) {
			if (!Objects.isNull(dbConfig.getDbMappers())) {
				this.mappers.addAll(dbConfig.getDbMappers());
			}
			if (!Objects.isNull(dbConfig.getDbGroups())) {
				dbConfig.getDbGroups().forEach(g ->
						this.dbGroupConfigs.put(g.getName(), g)
				);
			}
		}
	}

	public SessionContext getSessionContext(String name) {
		if (null == name) {
			name = DbGroup.defalutGroup;
		}
		GroupInfo groupInfo = sessionGroups.get(name);
		if (null == groupInfo) {
			synchronized (sessionGroupsLock) {
				groupInfo = sessionGroups.get(name);
				if (null == groupInfo) {
					Map<String, GroupInfo> newGroups = new HashMap<>(sessionGroups);
					groupInfo = getSessionGroupFromConfig(name);
					if (null == groupInfo) return null;
					newGroups.put(name, groupInfo);
					sessionGroups = newGroups;
				}
			}
		}
		return new SessionContext(getOneSlaveSession(groupInfo));

	}

	private GroupInfo getSessionGroupFromConfig(String name) {
		if (null == name) {
			name = DbGroup.defalutGroup;
		}
		DbGroup g = dbGroupConfigs.get(name);
		if (null == g) {
			return null;
		}
		List<SqlSessionFactory> l = initSqlSessionFactory(g);
		if (null == l || l.isEmpty()) {
			return null;
		}
		return new GroupInfo(l);
	}

	private List<SqlSessionFactory> initSqlSessionFactory(DbGroup g) {
		if (null == g.getInfoList() || g.getInfoList().isEmpty()) {
			return Collections.emptyList();
		}
		List<SqlSessionFactory> l = new ArrayList<>();
		g.getInfoList().forEach(i ->
				l.add(createSessionFactory(i))
		);
		return l;
	}

	private SqlSessionFactory createSessionFactory(DbProp config) {
		PooledDataSource dataSource =
				new PooledDataSource("com.mysql.cj.jdbc.Driver",
						"jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getName() + "?useUnicode=true&characterEncoding=utf8&useAffectedRows=true&serverTimezone=UTC",
						config.getUser(), config.getPassword());
		if (!Objects.isNull(config.getPoolMaximumActiveConnections())) {
			dataSource.setPoolMaximumActiveConnections(config.getPoolMaximumActiveConnections());
		}
		if (!Objects.isNull(config.getPoolMaximumIdleConnections())) {
			dataSource.setPoolMaximumIdleConnections(config.getPoolMaximumIdleConnections());
		}
		dataSource.setPoolPingEnabled(true);
		dataSource.setPoolPingQuery("select 1");
		dataSource.setPoolPingConnectionsNotUsedFor(1000 * 60 * 5);
		dataSource.setPoolTimeToWait(5000);

		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment(config.getEnvironment(), transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.setMapUnderscoreToCamelCase(true);
		for (String p : this.mappers) {
			configuration.addMappers(p);
		}
		return new SqlSessionFactoryBuilder().build(configuration);

	}

	private SqlSession getOneSlaveSession(GroupInfo groupInfo) {
		if (Objects.isNull(groupInfo) || groupInfo.factories.isEmpty()) {
			return null;
		}
		return groupInfo.factories.get(new Long(Math.abs(groupInfo.seq.getAndIncrement()) % groupInfo.factories.size()).intValue()).openSession();
	}

	public void addMapper(String mapper) {
		if (this.mappers.add(mapper)) {
			synchronized (sessionGroupsLock) {
				for (GroupInfo gi : sessionGroups.values()) {
					for (SqlSessionFactory f : gi.factories) {
						f.getConfiguration().addMappers(mapper);
					}
				}
			}
		}
	}

	public void addMapper(List<String> mappers) {
		if (null == mappers || mappers.isEmpty()) return;
		for (String mapper : mappers) {
			addMapper(mapper);
		}

	}

	public SessionContext getSessionContext() {
		return getSessionContext(null);
	}

}
