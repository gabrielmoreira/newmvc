package config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import lang.PropertyManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JPAConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource, JpaDialect jpaDialect, JpaVendorAdapter jpaVendorAdapter, Properties applicationProperties) {
		Properties jpaProperties = new PropertyManager(applicationProperties).getSection("jpa");
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setJpaDialect(jpaDialect);
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
		entityManagerFactory.setJpaProperties(jpaProperties);
		String[] packages = jpaProperties.getProperty("packages").split(",");
		entityManagerFactory.setPackagesToScan(packages);
		return entityManagerFactory;
	}

	@Bean
	public JpaDialect getJpaDialect() {
		HibernateJpaDialect jpaDialect = new HibernateJpaDialect();
		return jpaDialect;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public JpaVendorAdapter getJpaVendorAdapter(Properties applicationProperties) {
		Properties jpaProperties = new PropertyManager(applicationProperties).getSection("hibernate");
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(jpaProperties.getProperty("showSql").equals("true"));
		jpaVendorAdapter.setGenerateDdl(jpaProperties.getProperty("generateDdl").equals("true"));
		return jpaVendorAdapter;
	}
}
