package com.merchantvessel.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.merchantvessel.core.persistence.demo_data.DemoDataLoader;

@SpringBootApplication
public class MerchantvesselCoreApplication {

	public static void main(String[] args) {

		SpringApplication.run(MerchantvesselCoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner createModelData(DemoDataLoader loader) {
		return args -> {
			loader.createDemoData();
		};
	}

}
