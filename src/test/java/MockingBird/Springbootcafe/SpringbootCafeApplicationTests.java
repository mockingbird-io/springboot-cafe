package MockingBird.Springbootcafe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootTest
class SpringbootCafeApplicationTests {
	@Resource
	DataSource dataSource;
	@Test
	void contextLoads() throws Exception{
//		System.out.println("获取数据库链接为:" + dataSource.getConnection());
	}

}
