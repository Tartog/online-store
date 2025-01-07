package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.User;
import com.example.OnlineStore.service.MyUserDetailService;
import com.example.OnlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/store")
@AllArgsConstructor
@EnableMethodSecurity
public class PatchController {

    private UserService userService;
    private MyUserDetailService myUserDetailService;

    @PatchMapping("/{login}/updateUser")
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult){

        if (userService.emailExists(user.getEmail(), user.getId())) {
            bindingResult.rejectValue("email", "error.user", "Email must be unique.");
        }
        if (userService.loginExists(user.getLogin(), user.getId())) {
            bindingResult.rejectValue("login", "error.user", "Login must be unique.");
        }
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("editUser");
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        userService.updateUser(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getLogin());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());

        // Устанавливаем новую аутентификацию в SecurityContext
        SecurityContextHolder.getContext().setAuthentication(newAuth);


        return new ModelAndView("redirect:/api/v1/store");
    }
}
