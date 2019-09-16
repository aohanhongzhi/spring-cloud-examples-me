package com.peterwanghao.spring.cloud.vault;

import groovy.util.logging.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**   
 * @ClassName:  VaultSampleApplicationLiveTest
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: wanghao
 * @date:   2018年10月18日 下午4:58:49
 * @version V1.0
 * 
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class VaultSampleApplicationLiveTest {

	@Autowired
	Environment env;

	@Autowired
	DataSource datasource;

	@Value("${spring.cloud.vault.database.username-property}")
	String username;

	//@Test
	public void whenGenericBackendEnabled_thenEnvHasAccessToVaultSecrets() {

		String keyValue = env.getProperty("api_key");
		assertEquals("abc1234", keyValue);

	}

	//@Test
	public void whenKvBackendEnabled_thenEnvHasAccessToVaultSecrets() {

		String secretValue = env.getProperty("api_secret");
		assertEquals("1a2b3c4d", secretValue);

	}

	@Test
	public void whenDatabaseBackendEnabled_thenDatasourceUsesVaultCredentials() {
		System.out.println(username);

		try (Connection c = datasource.getConnection()) {

			ResultSet rs = c.createStatement().executeQuery("select 1");

			rs.next();
			Long value = rs.getLong(1);
			System.out.printf("访问得到的值：%s\n",value);

			assertEquals(Long.valueOf(1), value);

		} catch (SQLException sex) {
			throw new RuntimeException(sex);
		}
		System.out.println(username);

	}

}
