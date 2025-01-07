package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.DeliveryAddress;
import com.example.OnlineStore.model.Product;
import com.example.OnlineStore.model.ProductCategory;
import com.example.OnlineStore.model.User;
import com.example.OnlineStore.service.DeliveryAddressService;
import com.example.OnlineStore.service.ProductCategoryService;
import com.example.OnlineStore.service.ProductService;
import com.example.OnlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/store")
@AllArgsConstructor
@EnableMethodSecurity
public class PostController {

    private UserService userService;
    private DeliveryAddressService deliveryAddressService;
    private ProductCategoryService productCategoryService;
    private ProductService productService;

    @PostMapping
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult){
        if (userService.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "Email must be unique.");
        }
        if (userService.findByLogin(user.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.user", "Login must be unique.");
        }
        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("newUser");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        userService.saveUser(user);
        return new ModelAndView("redirect:/api/v1/store");
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/deliveryAddress/newAddress")
    public ModelAndView createAddress(@Valid DeliveryAddress deliveryAddress, BindingResult bindingResult){

        if(deliveryAddressService.existsAddress(deliveryAddress)){
            bindingResult.reject("error.address", "Address must be unique.");
        }

        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("newAddress");
            modelAndView.addObject("deliveryAddress", deliveryAddress);
            return modelAndView;
        }
        deliveryAddressService.saveDeliveryAddress(deliveryAddress);
        return new ModelAndView("redirect:/api/v1/store/deliveryAddress");
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @PostMapping("/productCategory/newCategory")
    public ModelAndView createProductCategory(@Valid ProductCategory productCategory, BindingResult bindingResult){
        if(productCategoryService.existsCategory(productCategory)){
            bindingResult.rejectValue("category", "error.category", "Product category must be unique.");
        }

        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("newCategory");
            modelAndView.addObject("productCategory", productCategory);
            return modelAndView;
        }
        productCategoryService.saveProductCategory(productCategory);
        return new ModelAndView("redirect:/api/v1/store/productCategory");
    }

    @PreAuthorize("hasAuthority('Seller')")
    @PostMapping("/product/newProduct")
    public ModelAndView createProduct(@Valid Product product, BindingResult bindingResult){
        if(productService.existsProduct(product)){
            bindingResult.rejectValue("name", "error.product", "Product name must be unique.");

        }
        if (bindingResult.hasErrors()){
            product.setProductCategories(new HashSet<>());
            ModelAndView modelAndView = new ModelAndView("newProduct");
            modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
            modelAndView.addObject("product", product);
            return modelAndView;
        }
        productService.saveProduct(product);
        return new ModelAndView("redirect:/api/v1/store/products/" + product.getUser().getLogin());
    }
}

