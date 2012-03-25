package config;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationConfigTest {
	private static AnnotationConfigApplicationContext context;

	@BeforeClass
	public static void setupClass() {
		context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
	}

	@Test
	public void testApplicationConfig() {
	}

}
