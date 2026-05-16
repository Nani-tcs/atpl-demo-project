package com.atpl.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>Welcome to ATPL Demo App!</h1><p>Application is running successfully.</p>";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}