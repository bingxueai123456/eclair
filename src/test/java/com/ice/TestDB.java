package com.ice;

import com.ice.eclair.db.SessionContext;
import com.ice.eclair.db.SessionFactory;
import com.ice.entity.User;
import com.ice.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: eclair
 * @Date: 2018/9/30 17:00
 * @Description:
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestDB {

	@Autowired
	SessionFactory sessionFactory;

	@Test
	public void testSelect() {
		SessionContext master = sessionFactory.getSessionContext("master");
		UserMapper mapper = master.getMapper(UserMapper.class);
		List<User> list = mapper.getList();
		System.out.println(list);
	}
}
