package com.example.OnlineStore.service;

import com.example.OnlineStore.config.MyUserDetails;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = repository.findByLogin(username);

        //return repository.findByLogin(username);
        //return null;
        return user.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        //        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
