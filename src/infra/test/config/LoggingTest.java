package config;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import config.sample.SampleBean2;
import config.sample.SampleController;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class LoggingTest {
	private static AnnotationConfigApplicationContext context;

	@BeforeClass
	public static void setupClass() {
		context = new AnnotationConfigApplicationContext(LoggingConfig.class, LoggingTest.class);
	}

	@Bean
	public SampleController getSampleController() {
		return new SampleController();
	}

	@Bean
	public SampleBean2 getSampleBean() {
		return new SampleBean2();
	}

	@Test
	public void testSampleControllerSucesso() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger log = loggerContext.getLogger(SampleController.class);
		@SuppressWarnings("unchecked")
		Appender<ILoggingEvent> appender = mock(Appender.class);
		log.addAppender(appender);
		try {
			SampleController sampleController = context.getBean(SampleController.class);
			sampleController.success();
		} finally {
			log.detachAppender(appender);
			verify(appender, times(2)).doAppend(any(ILoggingEvent.class));
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSampleControllerErro() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger log = loggerContext.getLogger(SampleController.class);
		@SuppressWarnings("unchecked")
		Appender<ILoggingEvent> appender = mock(Appender.class);
		log.addAppender(appender);
		try {
			SampleController sampleController = context.getBean(SampleController.class);
			sampleController.error();
		} catch (UnsupportedOperationException e) {
			ArgumentCaptor<ILoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
			verify(appender, times(3)).doAppend(loggingEventCaptor.capture());
			assertEquals("Erro de teste", loggingEventCaptor.getAllValues().get(1).getThrowableProxy().getMessage());
			throw e;
		} finally {
			log.detachAppender(appender);
		}
	}

	@Test
	public void testSampleControllerSucessoComDelay4Segundos() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger log = loggerContext.getLogger(SampleController.class);
		@SuppressWarnings("unchecked")
		Appender<ILoggingEvent> appender = mock(Appender.class);
		log.addAppender(appender);
		try {
			SampleController sampleController = context.getBean(SampleController.class);
			sampleController.successComDelay();
		} finally {
			log.detachAppender(appender);
			ArgumentCaptor<ILoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
			verify(appender, times(3)).doAppend(loggingEventCaptor.capture());
			ILoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(1);
			String performanceLogMessage = loggingEvent.getFormattedMessage();
			assertTrue(performanceLogMessage.contains("Tempo máximo de processamento"));
			assertTrue(performanceLogMessage.contains("SampleController.successComDelay()"));
			assertTrue(performanceLogMessage.contains("SampleBean2.doDelay()"));
			assertTrue(performanceLogMessage.contains("SampleBean2.doDelay()"));
			assertEquals("logging.performance", loggingEvent.getMarker().getName());
		}
	}

	@Test
	public void testSampleControllerSucessoComPoucoDelay() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger log = loggerContext.getLogger(SampleController.class);
		@SuppressWarnings("unchecked")
		Appender<ILoggingEvent> appender = mock(Appender.class);
		log.addAppender(appender);
		try {
			SampleController sampleController = context.getBean(SampleController.class);
			sampleController.successComPoucoDelay();
		} finally {
			log.detachAppender(appender);
			verify(appender, times(2)).doAppend(any(ILoggingEvent.class));
		}
	}

}
