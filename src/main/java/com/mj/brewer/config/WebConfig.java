package com.mj.brewer.config;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.mj.brewer.controller.CervejasController;
import com.mj.brewer.controller.converter.CidadeConverter;
import com.mj.brewer.controller.converter.EstadoConverter;
import com.mj.brewer.controller.converter.EstiloConverter;
import com.mj.brewer.controller.converter.GrupoConverter;
import com.mj.brewer.event.cache.CacheRemovalListener;
import com.mj.brewer.session.TabelasItemVenda;
import com.mj.brewer.thymeleaf.BrewerDialect;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@ComponentScan(basePackageClasses = { CervejasController.class, TabelasItemVenda.class })
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableCaching
@EnableAsync
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new Java8TimeDialect());
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new BrewerDialect());
		engine.addDialect(new DataAttributeDialect());
		engine.addDialect(new SpringSecurityDialect());

		return engine;
	}

	private ITemplateResolver templateResolver() {

		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}

	@Bean
	public FormattingConversionService mvcConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new EstiloConverter());
		conversionService.addConverter(new EstadoConverter());
		conversionService.addConverter(new CidadeConverter());
		conversionService.addConverter(new GrupoConverter());

		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00");
		NumberStyleFormatter intFormatter = new NumberStyleFormatter("#,##0");

		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		conversionService.addFormatterForFieldType(Integer.class, intFormatter);

		DateTimeFormatterRegistrar dateTimeFormatterRegistrar = new DateTimeFormatterRegistrar();
		dateTimeFormatterRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		dateTimeFormatterRegistrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm"));
		dateTimeFormatterRegistrar.registerFormatters(conversionService);

		return conversionService;
	}

	/**
	 * Utilizado para passar códigos para buscar classes de dominio no parâmetro do método controlador
	 * @return
	 */
	@Bean
	public DomainClassConverter<FormattingConversionService> domainClassConverter() {
		return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}

	@Bean
	public CacheManager cacheManager() {
		Caffeine<Object, Object> caffeine = Caffeine.newBuilder().maximumSize(2).expireAfterAccess(20, TimeUnit.SECONDS)
				.removalListener(new CacheRemovalListener()).recordStats();

		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.setCaffeine(caffeine);

		return cacheManager;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("classpath:/messages");
		bundle.setDefaultEncoding("UTF-8");
		return bundle;
	}
}
