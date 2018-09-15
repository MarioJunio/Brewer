package com.mj.brewer.config.init;

import java.util.EnumSet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {

		servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

		FilterRegistration.Dynamic fDynamic = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
		fDynamic.setInitParameter("encoding", "UTF-8");
		fDynamic.setInitParameter("forceEncoding", "true");
		fDynamic.addMappingForUrlPatterns(null, false, "/*");

	}

}
