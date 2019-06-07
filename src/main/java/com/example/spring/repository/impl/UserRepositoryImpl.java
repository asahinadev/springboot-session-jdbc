package com.example.spring.repository.impl;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spring.entity.User;
import com.example.spring.repository.UserRepository;

@Repository
public class UserRepositoryImpl
		extends AbstractRepository
		implements UserRepository {

	public static final String DELETE_SQL = "DELETE FROM users WHERE id = :id";

	public static final String UPDATE_SQL;
	static {

		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE users                                            ").append('\n');
		builder.append("SET    username            = :username           ").append('\n');
		builder.append("     , email               = :email              ").append('\n');
		builder.append("     , password            = :password           ").append('\n');
		builder.append("     , enabled             = :enabled            ").append('\n');
		builder.append("     , locked              = :locked             ").append('\n');
		builder.append("     , credentials_expired = :credentialsExpired ").append('\n');
		builder.append("     , account_expired     = :accountExpired     ").append('\n');
		builder.append("WHERE id = :id                                   ").append('\n');

		UPDATE_SQL = builder.toString();
	}

	public static final String INSERT_SQL;
	static {
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO users (        ").append('\n');
		builder.append("       id                  ").append('\n');
		builder.append("     , username            ").append('\n');
		builder.append("     , email               ").append('\n');
		builder.append("     , password            ").append('\n');
		builder.append("     , enabled             ").append('\n');
		builder.append("     , locked              ").append('\n');
		builder.append("     , credentials_expired ").append('\n');
		builder.append("     , account_expired     ").append('\n');
		builder.append(") VALUES (                 ").append('\n');
		builder.append("       :id                 ").append('\n');
		builder.append("     , :username           ").append('\n');
		builder.append("     , :email              ").append('\n');
		builder.append("     , :password           ").append('\n');
		builder.append("     , :enabled            ").append('\n');
		builder.append("     , :locked             ").append('\n');
		builder.append("     , :credentialsExpired ").append('\n');
		builder.append("     , :accountExpired     ").append('\n');
		builder.append(")                          ").append('\n');

		INSERT_SQL = builder.toString();

	}

	public static final String SELECT_SQL;

	public static final String SELECT_SQL_FIND_BY_ID;

	public static final String SELECT_SQL_FIND_BY_USERNAME;

	public static final String SELECT_SQL_FIND_BY_EMAIL;
	static {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT                      ").append('\n');
		builder.append("       id                   ").append('\n');
		builder.append("     , username             ").append('\n');
		builder.append("     , email                ").append('\n');
		builder.append("     , password             ").append('\n');
		builder.append("     , enabled              ").append('\n');
		builder.append("     , locked               ").append('\n');
		builder.append("     , credentials_expired  ").append('\n');
		builder.append("     , account_expired      ").append('\n');
		builder.append("  FROM users                ").append('\n');
		SELECT_SQL = builder.toString();

		builder = new StringBuilder(SELECT_SQL);
		builder.append(" WHERE username = :username ").append('\n');
		SELECT_SQL_FIND_BY_USERNAME = builder.toString();

		builder = new StringBuilder(SELECT_SQL);
		builder.append(" WHERE email    = :email    ").append('\n');
		SELECT_SQL_FIND_BY_EMAIL = builder.toString();

		builder = new StringBuilder(SELECT_SQL);
		builder.append(" WHERE id       = :id       ").append('\n');
		SELECT_SQL_FIND_BY_ID = builder.toString();

	}

	private final RowMapper<User> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
		return User.builder()
				.id(rs.getString("id"))
				.username(rs.getString("username"))
				.email(rs.getString("email"))
				.password(rs.getString("password"))
				.enabled(rs.getBoolean("enabled"))
				.locked(rs.getBoolean("locked"))
				.credentialsExpired(localDateTime(rs.getTimestamp("credentials_expired")))
				.accountExpired(localDateTime(rs.getTimestamp("account_expired")))
				.build();
	};

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Optional<User> findByUsername(String username) {

		return findBy(SELECT_SQL_FIND_BY_USERNAME, "username", username, ROW_MAPPER);
	}

	@Override
	public Optional<User> findByEmail(String email) {

		return findBy(SELECT_SQL_FIND_BY_EMAIL, "email", email, ROW_MAPPER);
	}

	@Override
	public Optional<User> findById(String id) {

		return findBy(SELECT_SQL_FIND_BY_ID, "id", id, ROW_MAPPER);
	}

	@Override
	public List<User> findAll() {

		return jdbcTemplate.query(SELECT_SQL, ROW_MAPPER);
	}

	@Override
	public User insert(User entity) {

		jdbcTemplate.update(INSERT_SQL, convert(entity));
		return entity;
	}

	@Override
	public List<User> insert(Iterable<User> entities) {

		jdbcTemplate.batchUpdate(INSERT_SQL, parameters(entities));
		return results(entities);
	}

	@Override
	public User update(User entity) {

		jdbcTemplate.update(UPDATE_SQL, convert(entity));
		return entity;
	}

	@Override
	public List<User> update(Iterable<User> entities) {

		jdbcTemplate.batchUpdate(UPDATE_SQL, parameters(entities));
		return results(entities);
	}

	@Override
	public User delete(User entity) {

		jdbcTemplate.update(DELETE_SQL, convert(entity));
		return entity;
	}

	@Override
	public List<User> delete(Iterable<User> entities) {

		jdbcTemplate.batchUpdate(DELETE_SQL, parameters(entities));
		return results(entities);
	}

}
