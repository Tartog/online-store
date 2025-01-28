package com.example.OnlineStore.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    public boolean hasAccess(Authentication authentication, String login) {
        String currentUserLogin = authentication.getName();
        return currentUserLogin.equals(login);
    }
}
