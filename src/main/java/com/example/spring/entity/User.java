package com.example.spring.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User
		implements UserDetails {

	String id;

	String username;

	String email;

	String password;

	boolean enabled;

	boolean locked;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime credentialsExpired;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime accountExpired;

	List<Roles> authorities;

	public List<Roles> getAuthorities() {

		if (authorities == null || authorities.isEmpty()) {
			return Arrays.asList(Roles.GUEST);
		}

		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {

		if (accountExpired == null) {
			return true;
		}
		return accountExpired.isAfter(LocalDateTime.now());
	}

	@Override
	public boolean isAccountNonLocked() {

		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		if (credentialsExpired == null) {
			return true;
		}
		return credentialsExpired.isAfter(LocalDateTime.now());
	}

}
