package com.jayanth.tradingplatform.controller;

import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.service.EmailService;
import com.jayanth.tradingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<User> sendVerificationOTP(@RequestHeader("Authorization") String jwt, @PathVariable Verificationtype verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);



        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two/verifyOTP/{otp}")
    public ResponseEntity<User> EnableTwoFactorAuth(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }





}
