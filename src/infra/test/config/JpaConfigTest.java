package config;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import config.sample.SampleBean;
import config.sample.SampleUser;

@Configuration
public class JpaConfigTest {

	private static AnnotationConfigApplicationContext context;

	@BeforeClass
	public static void setupClass() {
		context = new AnnotationConfigApplicationContext(DatabaseConfig.class, JPAConfig.class, JpaConfigTest.class);
	}

	@Bean(name = "applicationProperties")
	public Properties getProperties() {
		Properties prop = new Properties();
		prop.setProperty("datasource.jdbcUrl", "jdbc:h2:mem:test_mem");
		prop.setProperty("jpa.packages", "config.sample");
		prop.setProperty("hibernate.showSql", "true");
		prop.setProperty("hibernate.generateDdl", "true");
		return prop;
	}

	@Bean
	public SampleBean getSampleBean() {
		return new SampleBean();
	}

	@Test
	public void testTransactionManager() {
		PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
		Assert.assertNotNull(transactionManager);
		Assert.assertEquals(JpaTransactionManager.class, transactionManager.getClass());
	}

	@Test
	public void testEntityManagerFactory() {
		EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
		Assert.assertNotNull(emf);
	}

	@Test
	public void testEntityManager() {
		SampleBean sampleBean = context.getBean(SampleBean.class);
		Assert.assertNotNull(sampleBean);
		Assert.assertNotNull(sampleBean.getEm());
	}

	@Test
	public void testEntityManagerWriteAndRollback() {
		PlatformTransactionManager tm = context.getBean(PlatformTransactionManager.class);
		TransactionStatus transaction = tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		try {
			SampleBean sampleBean = context.getBean(SampleBean.class);
			Assert.assertNotNull(sampleBean);
			EntityManager em = sampleBean.getEm();
			Assert.assertNotNull(em);
			SampleUser user = new SampleUser();
			user.setNome("Gabriel");
			em.persist(user);
			Assert.assertEquals(user.getId(), new Long(1L));
			em.flush();
			user = em.find(SampleUser.class, 1L);
			Assert.assertEquals("Gabriel", user.getNome());
		} finally {
			tm.rollback(transaction);
		}
		TransactionStatus transaction2 = tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		try {
			SampleBean sampleBean = context.getBean(SampleBean.class);
			Assert.assertNotNull(sampleBean);
			EntityManager em = sampleBean.getEm();
			Assert.assertNotNull(em);
			SampleUser user = em.find(SampleUser.class, 1L);
			Assert.assertNull(user);
		} finally {
			tm.rollback(transaction2);
		}
	}

}
