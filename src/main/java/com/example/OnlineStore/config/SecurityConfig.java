package com.example.OnlineStore.config;

import com.example.OnlineStore.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        return new MyUserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/store/newUser/**",
                                "/api/v1/store",
                                "/Images/**",
                                "/api/v1/store/product/**",
                                "/js/**",
                                "/css/**",
                                "/fonts/**",
                                "/images/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form
                //        .loginPage("/login") // Укажите свой URL для страницы входа, если нужно
                        .defaultSuccessUrl("/api/v1/store", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/api/v1/store") // URL для перенаправления после успешного выхода
                        .invalidateHttpSession(true) // Уничтожить сессию после выхода
                        .deleteCookies("JSESSIONID")) // Удалить куки сессии
                .build();
    }
}
