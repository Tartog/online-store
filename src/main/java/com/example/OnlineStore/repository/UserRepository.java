package com.example.OnlineStore.repository;

import com.example.OnlineStore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByLoginAndIdNot(String login, Long id);
    void deleteUserByLogin(String login);
    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.email = ?3, u.login = ?4, u.passwordHash = ?5, " +
            "u.phoneNumber = ?6 where u.id = ?7")
    void setUserInfoById(String firstName, String lastName, String email, String login, String passwordHash,
                         String phoneNumber, Long id);
}
