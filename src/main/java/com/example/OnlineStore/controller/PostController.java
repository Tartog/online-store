package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.*;
import com.example.OnlineStore.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;

@RestController
@RequestMapping("/api/v1/store")
@AllArgsConstructor
@EnableMethodSecurity
public class PostController {

    private UserService userService;
    private DeliveryAddressService deliveryAddressService;
    private ProductCategoryService productCategoryService;
    private ProductService productService;
    private CartService cartService;

    @PostMapping
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult){
        if (userService.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "Такая почта уже зарегистрирована !");
        }
        if (userService.findByLogin(user.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.user", "Такой логин уже зарегистрирован !");
        }
        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("User/newUser");
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
            bindingResult.reject("error.address", "Такой адрес уже есть !");
        }

        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("Address/newAddress");
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
            bindingResult.rejectValue("category", "error.category", "Такая категория уже есть !");
        }

        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("Category/newCategory");
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
            bindingResult.rejectValue("name", "error.product", "Такое название товара уже есть !");

        }
        if(product.getProductCategories().size() == 0){
            bindingResult.rejectValue("productCategories", "error.product",
                    "Товар должен иметь хотя бы 1 категорию !");
        }
        if (bindingResult.hasErrors()){
            product.setProductCategories(new HashSet<>());
            ModelAndView modelAndView = new ModelAndView("Product/newProduct");
            modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
            modelAndView.addObject("product", product);
            return modelAndView;
        }
        productService.saveProduct(product);
        return new ModelAndView("redirect:/api/v1/store/products/" + product.getUser().getLogin());
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/cart/{login}/{productId}")
    public ModelAndView createProductInCart(@PathVariable String login, @PathVariable String productId){
        /*if(productService.existsProduct(product)){
            bindingResult.rejectValue("name", "error.product", "Такое название товара уже есть !");
        }*/

        Cart cart = new Cart();
        cart.setProduct(productService.findById(Long.parseLong(productId)));
        cart.setUser(userService.findByLogin(login));

        cart.setNumberOfProduct(1);

        /*if(product.getProductCategories().size() == 0){
            bindingResult.rejectValue("productCategories", "error.product",
                    "Товар должен иметь хотя бы 1 категорию !");
        }*/
        /*if (bindingResult.hasErrors()){
            product.setProductCategories(new HashSet<>());
            ModelAndView modelAndView = new ModelAndView("Product/newProduct");
            modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
            modelAndView.addObject("product", product);
            return modelAndView;
        }*/
        //productService.saveProduct(product);

        cartService.saveCart(cart);

        return new ModelAndView("redirect:/api/v1/store");
    }


}

