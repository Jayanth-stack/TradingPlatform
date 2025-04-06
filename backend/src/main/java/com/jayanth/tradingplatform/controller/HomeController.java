package com.jayanth.tradingplatform.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home(){
        return "Hello World, Welcome to Trading Platform";
    }
    @GetMapping("/api")
    public String secure(){
        return "Hello World, Welcome to Trading Platform";
    }
}
