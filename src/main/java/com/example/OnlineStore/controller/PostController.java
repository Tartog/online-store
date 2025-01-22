package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.*;
import com.example.OnlineStore.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private OrderService orderService;
    private ProductsInOrderService productsInOrderService;

    //@Value("${spring.web.resources.static-locations}")
    //private String staticLocations;

    @PostMapping
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult){
        if (userService.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "Такая почта уже зарегистрирована !");
        }
        if (userService.findByLogin(user.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.user", "Такой логин уже зарегистрирован !");
        }
        if (bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("html/User/newUser");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        userService.saveUser(user);
        return new ModelAndView("redirect:/api/v1/store");
    }

    /*@PreAuthorize("hasAuthority('Admin')")
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
        //return new ModelAndView("redirect:/api/v1/store/deliveryAddress");
        return new ModelAndView("redirect:/api/v1/store/deliveryAddressPage");
    }*/

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/deliveryAddress/newAddress")
    public ResponseEntity<String> createAddress(@RequestBody @Valid DeliveryAddress deliveryAddress, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; "); // Добавляем текст ошибки
            }
            return ResponseEntity.badRequest().body(errorMessage.toString()); // Возвращаем сообщения об ошибках
        }

        if (deliveryAddressService.existsAddress(deliveryAddress)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Такой адрес уже существует!"); // Возвращаем 409 при конфликте
        }

        deliveryAddressService.saveDeliveryAddress(deliveryAddress);
        return ResponseEntity.ok("Адрес успешно добавлен!"); // Возвращаем 200 OK
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Seller')")
    @PostMapping("/productCategory/newCategory")
    public ResponseEntity<String> createProductCategory(@RequestBody @Valid ProductCategory productCategory, BindingResult bindingResult){
        if(productCategoryService.existsProductCategoryByCategory(productCategory.getCategory())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Такая категория уже существует!");
        }
        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder("Ошибка валидации: ");
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        productCategoryService.saveProductCategory(productCategory);
        return ResponseEntity.ok("Категория успешно добавлена!");
    }

    @PreAuthorize("hasAuthority('Seller')")
    @PostMapping("/product/newProduct")
    public ModelAndView createProduct(@RequestParam("image") MultipartFile image, @Valid Product product, BindingResult bindingResult) {
        if (productService.existsProduct(product)) {
            bindingResult.rejectValue("name", "error.product", "Такое название товара уже есть !");
        }

        if (product.getProductCategories() == null || product.getProductCategories().isEmpty()) {
            bindingResult.rejectValue("productCategories", "error.product", "Товар должен иметь хотя бы 1 категорию !");
        }

        // Обработка загрузки изображения
        if (!image.isEmpty()) {
            String directoryPath = "C:/Users/Tartog/OnlineStore/Images/";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String imagePath = directoryPath + image.getOriginalFilename();
            File file = new File(imagePath);
            try {
                image.transferTo(file);
                product.setImagePath("/Images/" + image.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("imagePath", "error.imagePath", "Ошибка при добавлении изображения !");
            }
        }

        if (bindingResult.hasErrors()) {
            product.setProductCategories(new HashSet<>());
            ModelAndView modelAndView = new ModelAndView("html/Product/newProduct");
            modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
            modelAndView.addObject("product", product);
            return modelAndView;
        }

        productService.saveProduct(product);
        return new ModelAndView("redirect:/api/v1/store/products/" + product.getUser().getLogin());
    }

    /*@PreAuthorize("hasAuthority('Seller')")
    @PostMapping("/product/newProduct")
    public ModelAndView createProduct(@RequestParam("image") MultipartFile image, @Valid Product product, BindingResult bindingResult){
        if(productService.existsProduct(product)){
            bindingResult.rejectValue("name", "error.product", "Такое название товара уже есть !");

        }
        if(product.getProductCategories().size() == 0){
            bindingResult.rejectValue("productCategories", "error.product",
                    "Товар должен иметь хотя бы 1 категорию !");
        }

        if (!image.isEmpty()) {
            //String directoryPath = System.getProperty("user.dir") + "\\images\\";
            //System.out.println(directoryPath);
            //File directory = new File(directoryPath);
            String directoryPath = "C:/Users/Tartog/OnlineStore/Images/";
            File directory = new File(directoryPath);

            // Создаем директорию, если она не существует
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String imagePath = directoryPath + image.getOriginalFilename();
            //System.out.println(imagePath);
            //String imagePath = "src/main/resources/static/images/" + image.getOriginalFilename();
            //String imagePath = "/images/" + image.getOriginalFilename();
            //String imagePath = image.getOriginalFilename();
            File file = new File(imagePath);
            try {
                image.transferTo(file);
                //imagePath = "/images/" + image.getOriginalFilename();
                //imagePath = "/images/" + image.getOriginalFilename() + "?t=" + System.currentTimeMillis();
                //product.setImagePath(imagePath);
                //product.setImagePath("/images/" + image.getOriginalFilename());
                //product.setImagePath(imagePath);



                product.setImagePath("/Images/" + image.getOriginalFilename());
                //product.setImagePath("/" + image.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("imagePath", "error.imagePath",
                        "Ошибка при добавлении изображения !");
            }
        }

        if (bindingResult.hasErrors()){
            product.setProductCategories(new HashSet<>());
            ModelAndView modelAndView = new ModelAndView("html/Product/newProduct");
            modelAndView.addObject("listProductCategory", productCategoryService.findAllProductCategory());
            modelAndView.addObject("product", product);
            return modelAndView;
        }
        productService.saveProduct(product);
        return new ModelAndView("redirect:/api/v1/store/products/" + product.getUser().getLogin());
    }*/

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/cart/{login}/{productId}")
    public ModelAndView createProductInCart(@PathVariable String login, @PathVariable String productId){
        Product product = productService.findById(Long.parseLong(productId));
        User user = userService.findByLogin(login);

        if(cartService.productExist(product, user)){
            Cart cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setNumberOfProduct(1);
            cartService.saveCart(cart);
        }
        else{
            Cart cart = cartService.findByUserAndProduct(product, user);
            cart.setNumberOfProduct(cart.getNumberOfProduct() + 1);
            cartService.updateCart(cart);
        }
        return new ModelAndView("redirect:/api/v1/store");
    }

    @PreAuthorize("hasAuthority('User') and @userSecurity.hasAccess(authentication, #login)")
    @PostMapping("/order/{login}/newOrder")
    public ModelAndView createOrder(@PathVariable String login, @Valid Order order, BindingResult bindingResult){
        if(order.getDeliveryAddress() == null){
            bindingResult.rejectValue("deliveryAddress", "error.deliveryAddress",
                    "Необходимо выбрать адрес доставки !");
        }

        User user = userService.findByLogin(login);

        for(Cart cart : cartService.findAllByUser(user)){
            Product product = productService.findById(cart.getProduct().getId());
            if((product.getNumberOfProducts() - cart.getNumberOfProduct()) < 0){
                bindingResult.reject("error.NumberOfProduct", "На складе недостаточно товара " +
                        product.getName() + " !");
            }
        }

        if (bindingResult.hasErrors()){
            /*System.out.println("*********************************");
            System.out.println(order.getOrderDate());
            System.out.println(order.getOrderStatus());
            System.out.println(order.getId());
            System.out.println(order.getDeliveryAddress().getCity());
            System.out.println(order.getDeliveryAddress().getStreet());
            System.out.println(order.getDeliveryAddress().getHouseNumber());
            System.out.println(order.getDeliveryAddress().getId());
            System.out.println(order.getUser().getId());
            System.out.println(order.getUser().getLogin());
            System.out.println("*********************************");*/

            ModelAndView modelAndView = new ModelAndView("html/Order/newOrder");
            modelAndView.addObject("order", order);
            modelAndView.addObject("user", user);
            modelAndView.addObject("listAddress", deliveryAddressService.findAllDeliveryAddress());
            return modelAndView;
        }


        orderService.saveOrder(order);
        for(Cart cart : cartService.findAllByUser(user)){
            ProductsInOrder products = new ProductsInOrder();
            products.setProduct(cart.getProduct());
            products.setOrder(order);
            products.setNumberOfProduct(cart.getNumberOfProduct());

            Product product = productService.findById(cart.getProduct().getId());
            product.setNumberOfProducts(product.getNumberOfProducts() - cart.getNumberOfProduct());
            productService.updateProduct(product);

            productsInOrderService.saveProductsInOrder(products);
        }
        cartService.deleteAllByUser(user);

        return new ModelAndView("redirect:/api/v1/store");
    }

}

