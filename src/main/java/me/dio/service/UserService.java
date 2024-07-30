package me.dio.service;

import me.dio.domain.model.User;

import java.util.List;

public interface UserService extends CrudService<Long, User> {
    List<User> findByName(String name);
}