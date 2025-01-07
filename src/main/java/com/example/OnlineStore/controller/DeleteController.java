package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.User;
import com.example.OnlineStore.service.DeliveryAddressService;
import com.example.OnlineStore.service.ProductCategoryService;
import com.example.OnlineStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/store")
@AllArgsConstructor
@EnableMethodSecurity
public class DeleteController {

    private ProductCategoryService productCategoryService;
    private DeliveryAddressService deliveryAddressService;
    private ProductService productService;

    @DeleteMapping("productCategory/deleteCategory/{category}")
    public ModelAndView deleteCategory(@PathVariable String category){
        productCategoryService.deleteByCategory(category);
        return new ModelAndView("redirect:/api/v1/store/productCategory");
    }

    @DeleteMapping("deliveryAddress/deleteAddress/{addressId}")
    public ModelAndView deleteAddress(@PathVariable String addressId){
        deliveryAddressService.deleteDeliveryAddress(Long.parseLong(addressId));
        return new ModelAndView("redirect:/api/v1/store/deliveryAddress");
    }

    @DeleteMapping("products/deleteProduct/{productId}")
    public ModelAndView deleteProduct(@PathVariable String productId){
        User seller = productService.findById(Long.parseLong(productId)).getUser();
        productService.deleteProductByName(productService.findById(Long.parseLong(productId)).getName());
        return new ModelAndView("redirect:/api/v1/store/products/" + seller.getLogin());
    }
}
