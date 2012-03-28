package config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration
public class DatabaseTest {

	private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class, DatabaseTest.class);

	@Bean(name = "applicationProperties")
	public Properties getProperties() {
		Properties prop = new Properties();
		prop.setProperty("datasource.jdbcUrl", "jdbc:h2:mem:test_mem");
		return prop;
	}

	@Test
	public void deveExistirUmDataSourceQuandoObterDoSpring() throws Exception {
		// Quando
		DataSource dataSource = context.getBean(DataSource.class);
		// Ent�o
		Assert.assertNotNull(dataSource);
	}

	@Test
	public void deveExistirUmDataSourceTransactionManagerQuandoObterDoSpring() {
		// Quando
		PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
		// Ent�o
		Assert.assertNotNull(transactionManager);
		Assert.assertEquals(DataSourceTransactionManager.class, transactionManager.getClass());
	}

	@Test
	public void deveAbrirUmaNovaConexaoQuandoObterDataSourceDoSpring() throws Exception {
		// Dado
		DataSource dataSource = context.getBean(DataSource.class);
		// Quando
		Connection connection = dataSource.getConnection();
		// Ent�o
		Assert.assertNotNull(connection);
		connection.close();
	}

	@Test
	public void deveArmazenarInformacoesNoBancoQuandoExecutarInstrucoesSQL() throws Exception {
		// Dado
		DataSource dataSource = context.getBean(DataSource.class);
		Connection connection = dataSource.getConnection();
		// Quando
		connection.prepareStatement("CREATE TABLE USERS (name varchar);").execute();
		connection.prepareStatement("INSERT INTO USERS (name) VALUES ('GABRIEL');").execute();
		ResultSet rs = connection.prepareStatement("SELECT COUNT(*) FROM USERS").executeQuery();
		// Ent�o
		Assert.assertNotNull(rs);
		Assert.assertTrue(rs.next());
		Assert.assertEquals(1, rs.getLong(1));
		connection.close();
	}

	@Test
	public void deveRestaurarDadosDaTabelaQuandoRealizarRollbackDeUmaTransacao() throws Exception {
		// Dado
		PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
		TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		DataSource dataSource = context.getBean(DataSource.class);
		Connection connection = null;
		try {
			/*
			 * Aten��o, para que a conex�o seja gerenciada por uma transa��o do Spring,
			 * � necess�rio obter a conex�o usando  {@link DataSourceUtils#getConnection(DataSource)} 
			 * em vez da chamada padr�o no estilo J2EE {@link DataSource#getConnection()}.
			 * 
			 * Mais detalhes em {@link DataSourceUtils}.
			 */
			connection = DataSourceUtils.getConnection(dataSource);
			// Quando
			connection.prepareStatement("CREATE TABLE USERS2 (name varchar);").execute(); // CREATE TABLE n�o gera log transacional
			connection.prepareStatement("INSERT INTO USERS2 (name) VALUES ('GABRIEL');").execute();
		} finally {
			transactionManager.rollback(transaction);
			connection.close();
		}
		// Ent�o
		try {
			connection = dataSource.getConnection();
			ResultSet rs = connection.prepareStatement("SELECT COUNT(*) FROM USERS2").executeQuery();
			Assert.assertNotNull(rs);
			Assert.assertTrue(rs.next());
			Assert.assertEquals(0, rs.getLong(1));
		} finally {
			connection.close();
		}
	}
}
