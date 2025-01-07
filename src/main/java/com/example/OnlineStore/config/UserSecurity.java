package com.example.OnlineStore.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    public boolean hasAccess(Authentication authentication, String login) {
        // Получаем имя пользователя из аутентификации
        String currentUserLogin = authentication.getName();
        // Проверяем, совпадает ли текущий пользователь с указанным логином
        return currentUserLogin.equals(login);
    }
}
