package com.example.OnlineStore.controller;

import com.example.OnlineStore.DTO.Mapper.OrderMapper;
import com.example.OnlineStore.DTO.Mapper.ProductCategoryMapper;
import com.example.OnlineStore.DTO.OrderDTO;
import com.example.OnlineStore.DTO.ProductCategoryDTO;
import com.example.OnlineStore.model.*;
import com.example.OnlineStore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ModelAndView showDeliveryAddressesPage(@RequestParam(defaultValue = "0") int page) {
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
    public ResponseEntity<Page<DeliveryAddress>> getDeliveryAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) Integer houseNumber) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<DeliveryAddress> addresses;

        if (city == null && street == null && houseNumber == null) {
            addresses = deliveryAddressService.findAllDeliveryAddress(pageable);

        } else {
            addresses = deliveryAddressService.findByFilters(
                    (city != null && !city.isEmpty()) ? city : null,
                    (street != null && !street.isEmpty()) ? street : null,
                    houseNumber,
                    pageable
            );
        }

        return ResponseEntity.ok(addresses);
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

    /*@PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @GetMapping("/productCategory")
    public ModelAndView showProductCategoryPage(){
        ModelAndView modelAndView = new ModelAndView("html/Category/categoryPage");
        modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
        return modelAndView;
    }*/

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @GetMapping("/productCategoryPage")
    public ModelAndView showProductCategoryPage(@RequestParam(defaultValue = "0") int page){
        int pageSize = 10;
        Page<ProductCategory> productCategoryPage = productCategoryService.findAllProductCategory(PageRequest.of(page, pageSize));
        ModelAndView modelAndView = new ModelAndView("html/Category/categoryPage");

        if (productCategoryPage.isEmpty()) {
            modelAndView.addObject("message", "No addresses found.");
        } else {
            modelAndView.addObject("categories", productCategoryPage.getContent());
            modelAndView.addObject("totalPages", productCategoryPage.getTotalPages());
            modelAndView.addObject("currentPage", page);
        }
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Worker') or hasAuthority('Admin')")
    @GetMapping("/ordersPage")
    //public ModelAndView showOrders(@RequestParam("addressId") String addressId, @RequestParam(defaultValue = "0") int page){
    public ModelAndView showOrders(@RequestParam(required = false) Long id, @RequestParam(defaultValue = "0") int page){
        int pageSize = 10;
        Page<Order> orderPage;
        // = productCategoryService.findAllProductCategory(PageRequest.of(page, pageSize));
        ModelAndView modelAndView = new ModelAndView("html/Order/workerOrders");
        if(id == null) {
            orderPage = orderService.findAllOrders(PageRequest.of(page, pageSize));
        }
        else{
            //DeliveryAddress address = deliveryAddressService.findById(Long.parseLong(addressId));
            //orderPage = orderService.findAllOrdersByAddress(address, PageRequest.of(page, pageSize));
            orderPage = orderService.findAllOrdersById(id, PageRequest.of(page, pageSize));
        }
        if (orderPage.isEmpty()) {
            modelAndView.addObject("message", "No orders found.");
        } else {
            modelAndView.addObject("orders", orderPage.getContent());
            //modelAndView.addObject("totalPages", orderPage.getTotalPages());
            modelAndView.addObject("currentPage", page);
        }
        //modelAndView.addObject("listStatus", orderStatusService.findAllOrderStatus());
        //modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());
        //modelAndView.addObject("addressId", addressId);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('Worker') or hasAuthority('Admin')")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderDTO>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            //@RequestParam(required = false) String city,
            //@RequestParam(required = false) String street,
            //@RequestParam(required = false) Integer houseNumber,
            //@RequestParam(required = false) String status,
            @RequestParam(required = false) Long id) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Order> orders;
        //List<Order> orders;
        //List<DeliveryAddress> deliveryAddresses = deliveryAddressService.findByFilters(city, street, houseNumber);
        if (id == null) {
            orders = orderService.findAllOrders(pageable);
        } else {
            orders = orderService.findAllOrdersById(id, pageable);
            //DeliveryAddress address = deliveryAddressService.findById(Long.parseLong(addressId));
            //orders = orderService.findAllOrdersByAddress(address, pageable);
            //orders = orderService.findByFilters(city, street, houseNumber, status, id, pageable);
        }

        Page<OrderDTO> orderDTOs = orders.map(OrderMapper::toDTO);

        //Map<String, Object> response = new HashMap<>();
        //orders.getTotalPages();
        //int totalPages = (int) Math.ceil((double) orderService.getTotalCount(addressId) / size);
        //int totalPages = orders.getSize() / size;
        //response.put("listOrder", orders.getContent());
        //.put("totalPages", orders.getTotalPages());

        return ResponseEntity.ok(orderDTOs);
    }


    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/productCategory")
    public ResponseEntity<Page<ProductCategoryDTO>> getProductCategory(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProductCategory> categories;

        if (search != null && !search.isEmpty()) {
            categories = productCategoryService.findByCategoryContaining(search, pageable);
        } else {
            categories = productCategoryService.findAllProductCategory(pageable);
        }

        Page<ProductCategoryDTO> productCategoryDTOs = categories.map(ProductCategoryMapper::toDTO_FULL);

        return ResponseEntity.ok(productCategoryDTOs);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/productCategory/{id}")
    public ResponseEntity<ProductCategoryDTO> getProductCategoryById(@PathVariable Long id){
        ProductCategory productCategory = productCategoryService.findById(id);
        if (productCategory == null) {
            return ResponseEntity.notFound().build();
        }
        ProductCategoryDTO productCategoryDTO = ProductCategoryMapper.toDTO_FULL(productCategory);

        return ResponseEntity.ok(productCategoryDTO);
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

    @GetMapping("/test-image")
    public String testImage() {
        return "<img src='/images/Kinder.jpg' />";
    }
}
