package com.example.OnlineStore.controller;

import com.example.OnlineStore.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
@EnableMethodSecurity
public class TestController {
    /*@GetMapping("/welcome")
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

    @PostMapping("newUser")
    public String addUser(@RequestBody User user){
        return null;
    }*/
}
