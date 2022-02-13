package se.rydberg.bookmeeting;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;


@ComponentScan("se.rydberg.bookmeeting")
@SpringBootApplication
public class BookmeetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmeetingApplication.class, args);
	}

	@Bean
	public SpringSecurityDialect securityDialect(){
		return new SpringSecurityDialect();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
