package com.example.demo.web.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/demo")
    public String login() {
//        response.sendRedirect("index");
//        response.sendRedirect("index.html");
//        return "../resources/templates/index.html";

        return "demo";
    }
}
