package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.User;
import com.example.OnlineStore.repository.RoleRepository;
import com.example.OnlineStore.repository.UserRepository;
import com.example.OnlineStore.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
@Order(2)
public class UserServiceImpl implements UserService, CommandLineRunner {

    private UserRepository repository;
    private RoleRepository repositoryRole;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAllUser() {
        return repository.findAll();
    }

    @Override
    public User saveUser(User user) {
        //user.setPasswordHash("hello");
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return repository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        if(repository.findByLogin(login).isPresent()) {
            return repository.findByLogin(login).get();
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        if(repository.findByEmail(email).isPresent()) {
            return repository.findByEmail(email).get();
        }
        return null;
    }


    @Override
    @Transactional
    public void updateUser(User user) {
        /*repository.setUserInfoById(user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getDateOfBirth(), user.getId());*/
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        repository.setUserInfoById(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getLogin(),
                user.getPasswordHash(),
                user.getPhoneNumber(),
                user.getId());
        //user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        //repository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isEmailUnique(String email, Long id) {
        if(repository.findByEmail(email).isEmpty() || repository.findById(id).get().getEmail().equals(email)){
            return true;
        }
        return false; // Метод поиска пользователя по email
    }

    @Override
    public boolean isLoginUnique(String login, Long id) {

        if(repository.findByLogin(login).isEmpty() || repository.findById(id).get().getLogin().equals(login)){
            return true;
        }
        return false; // Метод поиска пользователя по email
        //return repository.findByLogin(login).isEmpty(); // Метод поиска пользователя по login
    }

    public boolean emailExists(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    public boolean loginExists(String login, Long id) {
        return repository.existsByLoginAndIdNot(login, id);
    }

    @Override
    public void run(String... args) throws Exception {
        User mainAdmin = new User();

        mainAdmin.setRole(repositoryRole.findByUserRole("Admin").get());
        mainAdmin.setEmail("admin@mail.ru");
        mainAdmin.setLogin("admin");
        mainAdmin.setPasswordHash("admin");
        mainAdmin.setPasswordHash(passwordEncoder.encode(mainAdmin.getPasswordHash()));

        if (repository.findByLogin(mainAdmin.getLogin()).isEmpty()){
            repository.save(mainAdmin);
        }
    }
}
