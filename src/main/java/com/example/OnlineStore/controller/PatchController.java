package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.Cart;
import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.service.CartService;
import com.example.OnlineStore.service.MyUserDetailService;
import com.example.OnlineStore.service.ProductService;
import com.example.OnlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private CartService cartService;
    private ProductService productService;

    @PatchMapping("/{login}/updateUser")
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult){

        if (userService.emailExists(user.getEmail(), user.getId())) {
            bindingResult.rejectValue("email", "error.user", "Такая почта уже зарегистрирована !");
        }
        if (userService.loginExists(user.getLogin(), user.getId())) {
            bindingResult.rejectValue("login", "error.user", "Такой логин уже зарегистрирован !");
        }
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("User/editUser");
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

    @PreAuthorize("hasAuthority('User') and authentication.name == #login")
    @PatchMapping("deleteProductInCart/{login}/{productId}")
    public ModelAndView updateProductInCart(@PathVariable String login, @PathVariable String productId){
        User user = userService.findByLogin(login);
        Product product = productService.findById(Long.parseLong(productId));
        Cart cart = cartService.findByUserAndProduct(product, user);
        if(cart.getNumberOfProduct() - 1 == 0){
            cartService.deleteCart(cart.getId());
        }
        else {
            cart.setNumberOfProduct(cart.getNumberOfProduct() - 1);
            cartService.updateCart(cart);
        }
        return new ModelAndView("redirect:/api/v1/store/cart/" + login);
    }
}
