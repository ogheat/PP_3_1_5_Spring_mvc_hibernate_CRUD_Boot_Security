package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface CustomUserRepository {
    List<User> getUsers();

    User getUserById(Long id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);
}
