package com.example.OnlineStore.config;

import com.example.OnlineStore.model.Role;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.service.MyUserDetailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private User user;

    public MyUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //Role role = new Role();
        //role.getUserRole();

        //System.out.println("******************************************************************");
        //System.out.println(Arrays.toString(user.getRole().getUserRole().split(", ")));
        //System.out.println("******************************************************************");

        return Arrays.stream(user.getRole().getUserRole().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        /*return Arrays.stream(user.getRole().getUserRole().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*/


        /*return Arrays.stream(user.getRole())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());*/

        //return null;

        //return Arrays.stream(user.getProducts().toString().split(", "))
        //        .map(SimpleGrantedAuthority::new)
        //        .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
        //return null;
    }

    @Override
    public String getUsername() {
        return user.getLogin();
        //return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        //return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        //return UserDetails.super.isEnabled();
        return true;
    }
}
