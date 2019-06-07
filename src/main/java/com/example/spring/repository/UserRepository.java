package com.example.spring.repository;

import java.util.List;
import java.util.Optional;

import com.example.spring.entity.User;

public interface UserRepository {

	public Optional<User> findByUsername(String username);

	public Optional<User> findByEmail(String email);

	public Optional<User> findById(String email);

	public List<User> findAll();

	public User insert(User entity);

	public List<User> insert(Iterable<User> entities);

	public User update(User entity);

	public List<User> update(Iterable<User> entities);

	public User delete(User entity);

	public List<User> delete(Iterable<User> entities);
}
