package config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DatabaseTest {

	private static AnnotationConfigApplicationContext context;

	@BeforeClass
	public static void setupClass() {
		context = new AnnotationConfigApplicationContext(DatabaseConfig.class, DatabaseTest.class);
	}

	@Bean(name = "applicationProperties")
	public Properties getProperties() {
		Properties prop = new Properties();
		prop.setProperty("datasource.jdbcUrl", "jdbc:h2:mem:test_mem");
		return prop;
	}

	@Test
	public void testDataSource() throws Exception {
		DataSource dataSource = context.getBean(DataSource.class);
		Assert.assertNotNull(dataSource);
	}

	@Test
	public void testTransaction() {
		PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
		Assert.assertNotNull(transactionManager);
	}

	@Test
	public void testConnection() throws Exception {
		DataSource dataSource = context.getBean(DataSource.class);
		Assert.assertNotNull(dataSource);
		Connection connection = dataSource.getConnection();
		Assert.assertNotNull(connection);
		connection.close();
	}

	@Test
	public void testSql() throws Exception {
		DataSource dataSource = context.getBean(DataSource.class);
		Assert.assertNotNull(dataSource);
		Connection connection = dataSource.getConnection();
		Assert.assertNotNull(connection);
		connection.prepareStatement("CREATE TABLE USERS (name varchar);").execute();
		connection.prepareStatement("INSERT INTO USERS (name) VALUES ('GABRIEL');").execute();
		ResultSet rs = connection.prepareStatement("SELECT COUNT(*) FROM USERS").executeQuery();
		Assert.assertNotNull(rs);
		rs.next();
		Assert.assertEquals(1, rs.getLong(1));
		connection.close();
	}
}
