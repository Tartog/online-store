package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.*;
import com.example.OnlineStore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@AllArgsConstructor
@EnableMethodSecurity
public class GetController {

    private UserService userService;
    private RoleService roleService;
    private MyUserDetailService myUserDetailService;
    private DeliveryAddressService deliveryAddressService;
    private ProductCategoryService productCategoryService;
    private ProductService productService;
    private CartService cartService;

    //private final UserServiceImpl userServiceImpl;

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/welcome")
    public ModelAndView findAllStudent(){
        //public List<Student> findAllStudent(){
        //modelAndView.addObject("students", studentService.findAllStudent());
        return new ModelAndView("hello");
        //return studentService.findAllStudent();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/test")
    public ModelAndView findAllStudent2(){
        //public List<Student> findAllStudent(){
        //modelAndView.addObject("students", studentService.findAllStudent());
        return new ModelAndView("hello");
        //return studentService.findAllStudent();
    }

    @GetMapping
    public ModelAndView showMainPage(){
        ModelAndView modelAndView = new ModelAndView("General/mainPage");
        Role user = roleService.findByUserRole("User").
                orElseThrow(() -> new RuntimeException("Отсутствует стандартная роль User"));//.get();
        Role seller = roleService.findByUserRole("Seller").
                orElseThrow(() -> new RuntimeException("Отсутствует стандартная роль Seller"));//.get();
        Role admin = roleService.findByUserRole("Admin").
                orElseThrow(() -> new RuntimeException("Отсутствует стандартная роль Admin"));//.get();
        Role worker = roleService.findByUserRole("Worker").
                orElseThrow(() -> new RuntimeException("Отсутствует стандартная роль Worker"));//.get();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals("anonymousUser")){
            modelAndView.addObject("currentUser", userService.findByLogin(auth.getName()));
            modelAndView.addObject("isAuthenticated", true);
        }
        else{
            modelAndView.addObject("isAuthenticated", false);
        }

        modelAndView.addObject("listOfProduct", productService.findAll());
        modelAndView.addObject("user", user);
        modelAndView.addObject("seller", seller);
        modelAndView.addObject("admin", admin);
        modelAndView.addObject("worker", worker);

        return modelAndView;
    }

    @GetMapping("/newUser/{role}")
    public ModelAndView newUser(@PathVariable String role){
        ModelAndView modelAndView = new ModelAndView("User/newUser");
        Role userRole = roleService.findByUserRole(role).
                orElseThrow(() -> new RuntimeException("Роль " + role + " отсутствует"));
        User user = new User();
        user.setRole(userRole);

        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PreAuthorize("@userSecurity.hasAccess(authentication, #login)")
    @GetMapping("/{login}/edit")
    public ModelAndView editUser(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("User/editUser");
        User user = userService.findByLogin(login);
        user.setPasswordHash(null);
        modelAndView.addObject("user", user);
        modelAndView.addObject("role", user.getRole());
        return modelAndView;
    }

    @PreAuthorize("@userSecurity.hasAccess(authentication, #login)")
    @GetMapping("/{login}/profile")
    public ModelAndView showProfile(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("General/profilePage");
        User user = userService.findByLogin(login);
        modelAndView.addObject("user", user);
        //modelAndView.addObject("role", user.getRole());
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddress")
    public ModelAndView showAddressPage(){
        ModelAndView modelAndView = new ModelAndView("Address/addressPage");
        modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @GetMapping("/productCategory")
    public ModelAndView showProductCategoryPage(){
        ModelAndView modelAndView = new ModelAndView("Category/categoryPage");
        modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/listOfUsers")
    public ModelAndView showListOfUsers(){
        ModelAndView modelAndView = new ModelAndView("User/listOfUsersPage");
        modelAndView.addObject("listOfUsers", userService.findAllUser());
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddress/newAddress")
    public ModelAndView newAddress(){
        ModelAndView modelAndView = new ModelAndView("Address/newAddress");
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        modelAndView.addObject("deliveryAddress", deliveryAddress);

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @GetMapping("/productCategory/newCategory")
    public ModelAndView newCategory(){
        ModelAndView modelAndView = new ModelAndView("Category/newCategory");
        ProductCategory productCategory = new ProductCategory();
        modelAndView.addObject("productCategory", productCategory);

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Seller') and @userSecurity.hasAccess(authentication, #login)")
    @GetMapping("/products/{login}")
    public ModelAndView showProductsPage(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("Product/productsPage");
        User seller = userService.findByLogin(login);
        modelAndView.addObject("seller", seller);
        modelAndView.addObject("listProduct", productService.findAllProduct(seller));

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Seller')")
    @GetMapping("/products/{login}/newProduct")
    public ModelAndView newProduct(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("Product/newProduct");
        User seller = userService.findByLogin(login);
        Product product = new Product();
        product.setUser(seller);
        //modelAndView.addObject("seller", seller);
        modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
        modelAndView.addObject("product", product);

        return modelAndView;
    }

    @GetMapping("/product/{productId}")
    public ModelAndView showProductPage(@PathVariable String productId){
        ModelAndView modelAndView = new ModelAndView("Product/productPage");
        Product product = productService.findById(Long.parseLong(productId));
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/cart/{login}")
    public ModelAndView showCart(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("Cart/cartPage");
        List<Cart> cart = cartService.findAllByUser(userService.findByLogin(login));
        int totalPrice = 0;
        for(Cart productInCart: cart){
            totalPrice += productInCart.getNumberOfProduct() * productInCart.getProduct().getPrice();
        }
        modelAndView.addObject("listCart", cart);
        modelAndView.addObject("totalPrice", totalPrice);
        return modelAndView;
    }
}
