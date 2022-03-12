package com.braindocs;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import javax.servlet.MultipartConfigElement;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@SpringBootApplication
public class BraindocsApplication {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT)
				.setFieldMatchingEnabled(true)
				.setSkipNullEnabled(true)
				.setFieldAccessLevel(PRIVATE)
				.setPropertyCondition(Conditions.isNotNull());
		return mapper;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.of(16, DataUnit.MEGABYTES));
		factory.setMaxRequestSize(DataSize.of(512, DataUnit.KILOBYTES));
		return factory.createMultipartConfig();
	}

	public static void main(String[] args) {
		SpringApplication.run(BraindocsApplication.class, args);
	}


}
