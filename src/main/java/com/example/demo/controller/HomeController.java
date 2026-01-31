package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/home"})
public String home(Model model) {
    model.addAttribute("message", "Hello from HomeController!");
    model.addAttribute("homeTitle", "Trang home sử dụng Thymeleaffff");
    return "index";
}
}
