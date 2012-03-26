package config;

import org.springframework.context.annotation.Import;

@Import(value = { DatabaseConfig.class, JPAConfig.class })
public class ApplicationConfig {

}
