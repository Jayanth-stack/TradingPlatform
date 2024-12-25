package com.jayanth.tradingplatform.controller;

import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.service.EmailService;
import com.jayanth.tradingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        return null;
    }

}
