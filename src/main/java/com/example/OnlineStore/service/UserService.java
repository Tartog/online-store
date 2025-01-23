package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Order;
import com.example.OnlineStore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    void deleteUserByLogin(String login);
    Page<User> findAllUsers(Pageable pageable);
    public boolean isEmailUnique(String email, Long id);
    public boolean isLoginUnique(String login, Long id);
}
