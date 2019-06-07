package com.example.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class BeanConfig {

	@Autowired
	MessageSource messageSource;

	@Autowired
	DataSource dataSource;

	@Bean
	public LocalValidatorFactoryBean validator() {

		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource);
		return validator;
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {

		return new NamedParameterJdbcTemplate(dataSource);
	}

}
