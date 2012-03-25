package config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
public class DatabaseConfig {

	@Bean(name = "dataSource")
	public DataSource getDataSource(Properties applicationProperties) throws Exception {
		return new BoneCPDataSource(new BoneCPConfig(new PropertyManager(applicationProperties).getSection("datasource")));
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager getTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
