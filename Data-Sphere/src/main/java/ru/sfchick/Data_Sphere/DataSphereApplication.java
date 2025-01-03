package ru.sfchick.Data_Sphere;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import ru.sfchick.Data_Sphere.config.PersonAccessHandler;

@SpringBootApplication
@EnableScheduling
public class DataSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataSphereApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
