package com.example.spring.repository.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.example.spring.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractRepository {

	protected static final DateTimeFormatter FORMAT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	protected static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	protected static final DateTimeFormatter FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;

	protected static SqlParameterSource[] parameters(Iterable<User> entities) {

		List<Map<String, Object>> parameters = new ArrayList<>();
		entities.forEach(entity -> {
			parameters.add((Map<String, Object>) convert(entity));
		});
		return SqlParameterSourceUtils.createBatch(parameters);
	}

	protected static <E> List<E> results(Iterable<E> entities) {

		List<E> results = new ArrayList<>();

		entities.forEach(entity -> {
			results.add(entity);
		});
		return results;
	}

	protected static LocalDateTime localDateTime(Date date) {

		if (date == null) {
			return null;
		}

		return LocalDateTime.parse(new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss").format(date));
	}

	protected static LocalDate localDate(Date date) {

		if (date == null) {
			return null;
		}

		return LocalDate.parse(new SimpleDateFormat("YYYY-MM-dd").format(date));
	}

	protected static LocalTime localTime(Date date) {

		if (date == null) {
			return null;
		}

		return LocalTime.parse(new SimpleDateFormat("HH:mm:ss").format(date));
	}

	protected static <E> Map<String, Object> convert(E entity) {

		Map<String, Object> map = new ObjectMapper().convertValue(entity, new TypeReference<Map<String, Object>>() {
			// nop
		});

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			log.debug("{} : {}", entry.getKey(), value == null ? "<NULL>" : value.getClass());
			map.put(entry.getKey(), value);

		}

		log.debug("entity : {}", entity);
		log.debug("map    : {}", map);

		return map;
	}

	protected <E> Optional<E> findBy(String sql, String key, String value, RowMapper<E> mapper) {

		try {
			return Optional.ofNullable(
					jdbcTemplate.queryForObject(sql, Map.of(key, value), mapper));
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return Optional.empty();
		}
	}

}
