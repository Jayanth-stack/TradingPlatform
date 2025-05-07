package com.jayanth.tradingplatform.controller;


import com.jayanth.tradingplatform.config.JwtProvider;
import com.jayanth.tradingplatform.model.TwoFactorOTP;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.UserRepository;
import com.jayanth.tradingplatform.response.AuthResponse;
import com.jayanth.tradingplatform.service.CustomUserDetailsService;
import com.jayanth.tradingplatform.service.EmailService;
import com.jayanth.tradingplatform.service.TwoFactorOTPService;
import com.jayanth.tradingplatform.utils.OtpUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user, HttpServletResponse response) throws Exception {
        // Extract user fields
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new Exception("email is already accessed");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFullName(fullName);

        User savedUser = userRepository.save(newUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                email,
                password
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        // Create the cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Only send over HTTPS
        jwtCookie.setPath("/"); // Cookie is valid for the entire domain
        jwtCookie.setDomain("localhost"); // Set the domain
        // Cannot use setSameSite as it's not supported in Jakarta Servlet Cookie class

        // Add the cookie to the response
        response.addCookie(jwtCookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(true);
        authResponse.setMessage("Success");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user, HttpServletResponse response) throws Exception {
        // Extract user fields
        String username = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        // Create the cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Only send over HTTPS
        jwtCookie.setPath("/"); // Cookie is valid for the entire domain	
        jwtCookie.setDomain("localhost"); // Set the domain
        // Cannot use setSameSite as it's not supported in Jakarta Servlet Cookie class

        // Add the cookie to the response
        response.addCookie(jwtCookie);

        User authUser = userRepository.findByEmail(username);

        if(user.getTwoFactorAuth() != null && user.getTwoFactorAuth().isEnabled()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two Factor Auth is enabled");
            authResponse.setIsTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();
            TwoFactorOTP oldTwoFactorOTP = twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwoFactorOTP != null){
                twoFactorOTPService.deleteTwoFactorOTP(oldTwoFactorOTP);
            }
            TwoFactorOTP newTwoFactorOTP = twoFactorOTPService.createTwoFactorOTP(authUser, otp, jwt);
            emailService.sendVerificationOTPEmail(username, otp);
            authResponse.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(true);
        authResponse.setMessage("Login Success");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        if(!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/twofactor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp, @RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyTwoFactorOTP(twoFactorOTP, otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor Auth verified");
            res.setIsTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("invalid OTP");
    }
}
