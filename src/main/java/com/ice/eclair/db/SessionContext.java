package com.ice.eclair.db;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.Objects;

/**
 * @Auther: eclair
 * @Date: 2018/9/5 23:32
 * @Description:
 */
@Slf4j
public class SessionContext {
	private SqlSession sqlSession;

	public SessionContext(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public static void closeSilently(SessionContext sessionContext) {
		if (Objects.isNull(sessionContext)) {
			return;
		}
		try {
			sessionContext.close();
		} catch (Throwable e) {
			log.error("db closeSilently", e);
		}
	}

	public static void rollbackSilently(SessionContext ctx) {
		if (Objects.isNull(ctx)) {
			return;
		}
		try {
			ctx.rollback();
		} catch (Throwable e) {
			log.error("rollbackSilently", e);
		}
	}

	public void commit() {
		if (!Objects.isNull(sqlSession)) {
			sqlSession.commit();
		}
	}

	public void rollback() {
		if (sqlSession != null) {
			sqlSession.rollback();
		}
	}

	public void close() {
		if (sqlSession != null) {
			sqlSession.close();
			sqlSession = null;
		}
	}

	public <T> T getMapper(Class<T> clazz) {
		return sqlSession.getMapper(clazz);
	}

}
