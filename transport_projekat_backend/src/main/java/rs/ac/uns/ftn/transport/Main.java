package rs.ac.uns.ftn.transport;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	@Bean
	public Validator validator() {
		try (ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory()) {
			return validatorFactory.getValidator();
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
