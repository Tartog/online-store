package com.example.OnlineStore.controller;

import com.example.OnlineStore.DTO.Mapper.OrderMapper;
import com.example.OnlineStore.DTO.OrderDTO;
import com.example.OnlineStore.model.*;
import com.example.OnlineStore.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    private OrderService orderService;
    private OrderStatusService orderStatusService;
    private DeliveryAddressService deliveryAddressService;
    private ProductCategoryService productCategoryService;

    @PatchMapping("/{login}/updateUser")
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult){

        if (userService.emailExists(user.getEmail(), user.getId())) {
            bindingResult.rejectValue("email", "error.user",
                    "Такая почта уже зарегистрирована !");
        }
        if (userService.loginExists(user.getLogin(), user.getId())) {
            bindingResult.rejectValue("login", "error.user",
                    "Такой логин уже зарегистрирован !");
        }
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("html/User/editUser");
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        userService.updateUser(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getLogin());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(),
                userDetails.getAuthorities());

        // Устанавливаем новую аутентификацию в SecurityContext
        SecurityContextHolder.getContext().setAuthentication(newAuth);


        return new ModelAndView("redirect:/api/v1/store");
    }

    @PreAuthorize("hasAuthority('User') and authentication.name == #login")
    @PatchMapping("addProductToCart/{login}/{productId}")
    @ResponseBody
    public ResponseEntity<String> addProductToCart(@PathVariable String login, @PathVariable String productId) {
        User user = userService.findByLogin(login);
        Product product = productService.findById(Long.parseLong(productId));
        Cart cart = cartService.findByUserAndProduct(product, user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser (user);
            cart.setProduct(product);
            cart.setNumberOfProduct(1);
            cartService.saveCart(cart);
        } else {
            cart.setNumberOfProduct(cart.getNumberOfProduct() + 1);
            cartService.updateCart(cart);
        }
        return ResponseEntity.ok("Товар добавлен в корзину");
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("deliveryAddress/updateAddress")
    public ResponseEntity<String> updateAddress(@RequestBody @Valid DeliveryAddress address, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        deliveryAddressService.updateDeliveryAddress(address);
        return ResponseEntity.ok("Адрес успешно обновлен!");
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @PatchMapping("productCategory/updateProductCategory")
    public ResponseEntity<String> updateCategory(@RequestBody @Valid ProductCategory productCategory, BindingResult bindingResult) {
        if (productCategoryService.existsByCategoryAndIdNot(productCategory.getCategory(), productCategory.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Такая категория уже существует!");
        }
        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }

        productCategoryService.updateProductCategory(productCategory);
        return ResponseEntity.ok("Категория успешно обновлена!");
    }

    @PreAuthorize("hasAuthority('Worker') or hasAuthority('Admin')")
    @PatchMapping("/orders/updateOrderStatus/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable("id") Long orderId, @RequestBody String status){
        Order order = orderService.findById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        status = status.substring(1, status.length() - 1);
        String finalStatus = status;
        order.setOrderStatus(orderStatusService.findByStatus(status).orElseThrow(()->
                new RuntimeException("Отсутствует статус заказа " + finalStatus + " !")));
        orderService.updateOrder(order);

        return ResponseEntity.ok(OrderMapper.toDTO(order));
    }
}
