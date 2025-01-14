package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.*;
import com.example.OnlineStore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private OrderService orderService;
    private OrderStatusService orderStatusService;
    private ProductsInOrderService productsInOrderService;

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
        ModelAndView modelAndView = new ModelAndView("html/General/mainPage");
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
        ModelAndView modelAndView = new ModelAndView("html/User/newUser");
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
        ModelAndView modelAndView = new ModelAndView("html/User/editUser");
        User user = userService.findByLogin(login);
        user.setPasswordHash(null);
        modelAndView.addObject("user", user);
        modelAndView.addObject("role", user.getRole());
        return modelAndView;
    }

    @PreAuthorize("@userSecurity.hasAccess(authentication, #login)")
    @GetMapping("/{login}/profile")
    public ModelAndView showProfile(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("html/General/profilePage");
        User user = userService.findByLogin(login);
        modelAndView.addObject("user", user);
        //modelAndView.addObject("role", user.getRole());
        return modelAndView;
    }

    /*@PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddress")
    public ModelAndView showAddressPage(){
        ModelAndView modelAndView = new ModelAndView("html/Address/addressPage");
        modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());
        return modelAndView;
    }*/

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddressPage")
    public ModelAndView getDeliveryAddressesPage(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 10; // количество адресов на странице
        Page<DeliveryAddress> addressesPage = deliveryAddressService.findAllDeliveryAddress(PageRequest.of(page, pageSize));

        ModelAndView modelAndView = new ModelAndView("html/Address/addressPage"); // Имя вашего шаблона

        if (addressesPage.isEmpty()) {
            modelAndView.addObject("message", "No addresses found.");
        } else {
            modelAndView.addObject("addresses", addressesPage.getContent());
            modelAndView.addObject("totalPages", addressesPage.getTotalPages());
            modelAndView.addObject("currentPage", page);
        }

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddress")
    public ResponseEntity<Page<DeliveryAddress>> getDeliveryAddresses(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 10; // количество адресов на странице
        Page<DeliveryAddress> addressesPage = deliveryAddressService.findAllDeliveryAddress(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(addressesPage);
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @GetMapping("/productCategory")
    public ModelAndView showProductCategoryPage(){
        ModelAndView modelAndView = new ModelAndView("html/Category/categoryPage");
        modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/listOfUsers")
    public ModelAndView showListOfUsers(){
        ModelAndView modelAndView = new ModelAndView("html/User/listOfUsersPage");
        modelAndView.addObject("listOfUsers", userService.findAllUser());
        return modelAndView;
    }

    /*@PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddress/newAddress")
    public ModelAndView newAddress(){
        ModelAndView modelAndView = new ModelAndView("html/Address/newAddress");
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        modelAndView.addObject("deliveryAddress", deliveryAddress);

        return modelAndView;
    }*/

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @GetMapping("/productCategory/newCategory")
    public ModelAndView newCategory(){
        ModelAndView modelAndView = new ModelAndView("html/Category/newCategory");
        ProductCategory productCategory = new ProductCategory();
        modelAndView.addObject("productCategory", productCategory);

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Seller') and @userSecurity.hasAccess(authentication, #login)")
    @GetMapping("/products/{login}")
    public ModelAndView showProductsPage(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("html/Product/productsPage");
        User seller = userService.findByLogin(login);
        modelAndView.addObject("seller", seller);
        modelAndView.addObject("listProduct", productService.findAllProduct(seller));

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Seller')")
    @GetMapping("/products/{login}/newProduct")
    public ModelAndView newProduct(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("html/Product/newProduct");
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
        ModelAndView modelAndView = new ModelAndView("html/Product/productPage");
        Product product = productService.findById(Long.parseLong(productId));
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('User') and authentication.name == #login")
    @GetMapping("/cart/{login}")
    public ModelAndView showCart(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("html/Cart/cartPage");
        User user = userService.findByLogin(login);
        List<Cart> cart = cartService.findAllByUser(user);
        double totalPrice = 0;
        for(Cart productInCart: cart){
            totalPrice += productInCart.getNumberOfProduct() * productInCart.getProduct().getPrice();
        }
        if(cartService.findAllByUser(user).size() == 0){
            modelAndView.addObject("cartNotEmpty", false);
        }
        else{
            modelAndView.addObject("cartNotEmpty", true);
        }
        modelAndView.addObject("listCart", cart);
        modelAndView.addObject("totalPrice", totalPrice);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('User') and authentication.name == #login")
    @GetMapping("/order/{login}/newOrder")
    public ModelAndView newOrder(@PathVariable String login){
        User user = userService.findByLogin(login);

        ModelAndView modelAndView = new ModelAndView("html/Order/newOrder");

        Order order = new Order();
        order.setUser(user);

        order.setOrderStatus(orderStatusService
                .findByStatus("Доставляется")
                .orElseThrow(()-> new RuntimeException("Отсутствует стандартный статус 'Доставляется' !")));
        //modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());

        LocalDate localDate = LocalDate.now(); // текущая дата
        Date sqlDate = Date.valueOf(localDate); // преобразование в java.sql.Dat

        order.setOrderDate(sqlDate);
        order.setExpectedReceiveDate(Date.valueOf(localDate.plusDays(7)));

        modelAndView.addObject("order", order);
        modelAndView.addObject("user", user);
        modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());

        return modelAndView;
    }

    @PreAuthorize("hasAuthority('User') and authentication.name == #login")
    @GetMapping("/order/{login}/orders")
    public ModelAndView showUserOrders(@PathVariable String login){
        ModelAndView modelAndView = new ModelAndView("html/Order/userOrders");
        User user = userService.findByLogin(login);
        /*double totalPrice = 0;
        List<Order> orders = orderService.findAllByUser(user);
        for(Order order: orders){
            for(ProductsInOrder productsInOrder: productsInOrderService.findAllProductsInOrder(order)){
                totalPrice += productsInOrder.get
            }
        }*/

        //modelAndView.addObject("totalPrice", totalPrice);
        modelAndView.addObject("listOrder", orderService.findAllByUser(user));
        return modelAndView;
    }

    /*@PreAuthorize("hasAuthority('Worker') or hasAuthority('Admin')")
    @GetMapping("/orders/{addressId}")
    public ModelAndView showOrders(@PathVariable String addressId){
        ModelAndView modelAndView = new ModelAndView("Order/ordersToAddress");

        if(addressId.equals("all")){
            modelAndView.addObject("listOrder", orderService.findAllOrder());
        }
        else {
            DeliveryAddress address = deliveryAddressService.findById(Long.parseLong(addressId));
            modelAndView.addObject("listOrder", orderService.findAllByAddress(address));
        }


        //System.out.println("*****************************************************************");
        //System.out.println(orderService.findAllByAddress(address).size());
        //System.out.println("*****************************************************************");

        modelAndView.addObject("listStatus", orderStatusService.findAllOrderStatus());
        modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());
        return modelAndView;
    }*/

    @PreAuthorize("hasAuthority('Worker') or hasAuthority('Admin')")
    @GetMapping("/orders")
    public ModelAndView showOrders(@RequestParam("addressId") String addressId){
        ModelAndView modelAndView = new ModelAndView("html/Order/ordersToAddress");
        if(addressId.equals("all")) {
            modelAndView.addObject("listOrder", orderService.findAllOrder());
        }
        else{
            DeliveryAddress address = deliveryAddressService.findById(Long.parseLong(addressId));
            modelAndView.addObject("listOrder", orderService.findAllByAddress(address));
        }
        modelAndView.addObject("listStatus", orderStatusService.findAllOrderStatus());
        modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());
        modelAndView.addObject("addressId", addressId);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/deliveryAddress/{id}")
    public ResponseEntity<DeliveryAddress> getAddressById(@PathVariable Long id) {
        DeliveryAddress address = deliveryAddressService.findById(id);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(address);
    }

    @GetMapping("/test-image")
    public String testImage() {
        return "<img src='/images/Kinder.jpg' />";
    }
}
