package com.dgp.elasticsearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsMarkerConfiguration {

	@Bean
	public Marker markerBean() {
		return new Marker();
	}

	class Marker {
	}

}
