package com.dgp.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaMarkerConfiguration {
	
	@Bean
	public Marker kafkaMarkerBean() {
		return new Marker();
	}

	class Marker {
	}

}
