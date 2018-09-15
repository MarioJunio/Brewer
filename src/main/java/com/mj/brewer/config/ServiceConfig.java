package com.mj.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mj.brewer.event.CervejaEventListener;
import com.mj.brewer.service.CervejaService;
import com.mj.brewer.storage.FotoStorageLocal;
import com.mj.brewer.storage.IFotoStorage;

@Configuration
@ComponentScan(basePackageClasses = CervejaService.class)
public class ServiceConfig {

	@Bean
	public IFotoStorage fotoStorage() {
		return new FotoStorageLocal();
	}
	
	@Bean
	public CervejaEventListener cervejaEventListener() {
		return new CervejaEventListener();
	}
}
