package br.com.simplewpps.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
public class SimplewppsApplication {
	
	@Value("${front-end.url}")
	private String urlFrontEnd;

	public static void main(String[] args) {
		SpringApplication.run(SimplewppsApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*")
				.allowCredentials(true)
				.maxAge(86400)
				.allowedOrigins(urlFrontEnd)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "TRACE")
				.allowedHeaders("Content-Type", "Accept");
			}
		};
	}

}
