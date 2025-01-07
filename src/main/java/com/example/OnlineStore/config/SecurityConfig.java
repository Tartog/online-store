package com.example.OnlineStore.config;

import com.example.OnlineStore.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        /*UserDetails admin = User.builder().username("admin").password(encoder.encode("admin")).roles("ADMIN").
                build();
        UserDetails user = User.builder().username("user").password(encoder.encode("user")).roles("USER").
                build();
        UserDetails anton32 = User.builder().username("anton32").password(encoder.encode("password1")).roles("ADMIN", "USER").
                build();

        return new InMemoryUserDetailsManager(admin, user, anton32);*/

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

    //указываем где разрешить доступ всем, а где всем авторизованным пользователям
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                //"/api/v1/store/welcome",
                                "/api/v1/store/newUser/**",
                                "/api/v1/store"
                        )
                        .permitAll()
                        //.requestMatchers("/api/v1/store/abc"
                        //)
                        //.requestMatchers("/api/v1/store/**/edit")
                        //.access("@userSecurity.hasAccess(authentication, #login)")
                        .anyRequest()
                        .authenticated())
                        //.requestMatchers("/api/v1/store/**").authenticated())
                //.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .formLogin(form -> form
                //        .loginPage("/login") // Укажите свой URL для страницы входа, если нужно
                        .defaultSuccessUrl("/api/v1/store", true))
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода из аккаунта
                        //.logoutSuccessUrl("/login?logout") // URL для перенаправления после успешного выхода
                        .logoutSuccessUrl("/api/v1/store") // URL для перенаправления после успешного выхода
                        .invalidateHttpSession(true) // Уничтожить сессию после выхода
                        .deleteCookies("JSESSIONID")) // Удалить куки сессии
                .build();
    }
}
