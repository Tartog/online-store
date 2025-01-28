package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/store")
@AllArgsConstructor
@EnableMethodSecurity
public class DeleteController {

    private ProductCategoryService productCategoryService;
    private DeliveryAddressService deliveryAddressService;
    private ProductService productService;
    private UserService userService;
    private CartService cartService;

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("productCategory/deleteCategory/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId){
        productCategoryService.deleteProductCategory(Long.parseLong(categoryId));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("deliveryAddress/deleteAddress/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String addressId) {
        deliveryAddressService.deleteDeliveryAddress(Long.parseLong(addressId));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('Seller')")
    @DeleteMapping("products/deleteProduct/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(Long.parseLong(productId));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('Admin') or authentication.name == #login")
    @DeleteMapping("deleteUser/{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByLogin(auth.getName());

        userService.deleteUserByLogin(login);
        if(currentUser.getRole().getUserRole().equals("Admin")){
            return ResponseEntity.noContent().build();
        }

        URI location = URI.create("/logout");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }

    @PreAuthorize("hasAuthority('User') and authentication.name == #login")
    @DeleteMapping("deleteProductInCart/{login}/{productId}")
    public ModelAndView deleteProductInCart(@PathVariable String login, @PathVariable String productId){
        User user = userService.findByLogin(login);
        Product product = productService.findById(Long.parseLong(productId));
        cartService.deleteCart(cartService.findByUserAndProduct(product, user).getId());
        return new ModelAndView("redirect:/api/v1/store/cart/" + login);
    }
}
