package config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingConfig {

	@Around("execution(public * *(..))")
	public Object logging(ProceedingJoinPoint pjp) throws Throwable {
		Logger log = LoggerFactory.getLogger(pjp.getTarget().getClass());
		try {
			log.info("Iniciando método {}", pjp.getSignature().toShortString());
			return pjp.proceed();
		} catch (Throwable t) {
			log.error("Erro no método " + pjp.getSignature().toShortString(), t);
			throw t;
		} finally {
			log.info("Finalizando método {}", pjp.getSignature().toShortString());
		}
	}
}
