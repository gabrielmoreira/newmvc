package config;

import lang.TreeStopWatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingConfig {

	private static final ThreadLocal<TreeStopWatch> STOP_WATCH = new ThreadLocal<TreeStopWatch>();
	private static final Marker PERFORMANCE = MarkerFactory.getMarker("logging.performance");
	private static final Marker BASIC = MarkerFactory.getMarker("logging.basic");

	private boolean loggingPerformance = true;
	private int loggingPerformanceThreshould = 2000;

	@Around("execution(public * *(..))")
	public Object logging(ProceedingJoinPoint pjp) throws Throwable {
		Logger log = LoggerFactory.getLogger(pjp.getTarget().getClass());
		String methodName = pjp.getSignature().toShortString();
		long start = System.currentTimeMillis();
		try {
			log.info(BASIC, "Iniciando método {}", methodName);
			return loggingPerformance ? performanceLogging(log, pjp) : pjp.proceed();
		} catch (Throwable t) {
			log.error(BASIC, "Erro no método " + methodName, t);
			throw t;
		} finally {
			log.info(BASIC, "Finalizando método {} : duração {} ms ", methodName, (System.currentTimeMillis() - start));
		}
	}

	public Object performanceLogging(Logger log, ProceedingJoinPoint pjp) throws Throwable {
		TreeStopWatch stopWatch = STOP_WATCH.get();
		boolean isRoot = stopWatch == null;
		if (isRoot) {
			stopWatch = new TreeStopWatch();
			STOP_WATCH.set(stopWatch);
		}
		try {
			stopWatch.startEvent(pjp.getSignature().toShortString());
			return pjp.proceed();
		} finally {
			stopWatch.stopEvent();
			if (isRoot) {
				STOP_WATCH.set(null);
				if (stopWatch.getTotalDuration() > loggingPerformanceThreshould) {
					log.warn(PERFORMANCE, "Tempo máximo de processamento {} foram gastos em:\n{}", loggingPerformanceThreshould, stopWatch.prettyPrint());
				}
			}
		}
	}

}
