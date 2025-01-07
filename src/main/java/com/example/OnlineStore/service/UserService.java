package com.example.OnlineStore.service;

import com.example.OnlineStore.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();
    User saveUser(User user);
    User findByLogin(String login);
    User findByEmail(String email);
    void updateUser(User user);
    void deleteUser(Long id);
    boolean emailExists(String email, Long id);
    boolean loginExists(String login, Long id);
    public boolean isEmailUnique(String email, Long id);
    public boolean isLoginUnique(String login, Long id);
}
