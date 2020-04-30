package com.mj.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mj.brewer.event.CervejaEventListener;
import com.mj.brewer.event.VendaEventListener;
import com.mj.brewer.service.CervejaService;
import com.mj.brewer.storage.FotoStorage;

@Configuration
@ComponentScan(basePackageClasses = {CervejaService.class, FotoStorage.class})
public class ServiceConfig {

	@Bean
	public CervejaEventListener cervejaEventListener() {
		return new CervejaEventListener();
	}
	
	@Bean
	public VendaEventListener vendaEventListener() {
		return new VendaEventListener();
	}
}
