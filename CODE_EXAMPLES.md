# Trading Platform - Code Examples for Critical Fixes

This document provides specific code changes needed for the top critical issues.

---

## 1. FIX JWT TOKEN HANDLING

### Backend Changes

#### Step 1: Create Request/Response DTOs

**File: `backend/src/main/java/com/jayanth/tradingplatform/request/LoginRequest.java`**
```java
package com.jayanth.tradingplatform.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

**File: `backend/src/main/java/com/jayanth/tradingplatform/request/SignupRequest.java`**
```java
package com.jayanth.tradingplatform.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
    
    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

**File: `backend/src/main/java/com/jayanth/tradingplatform/response/AuthResponse.java`** (Updated)
```java
package com.jayanth.tradingplatform.response;

public class AuthResponse {
    private boolean status;
    private String message;
    private String jwt;  // ADD THIS - return JWT in response body
    private String session;
    private boolean twoFactorAuthEnabled;
    
    // Getters and Setters
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getJwt() { return jwt; }
    public void setJwt(String jwt) { this.jwt = jwt; }
    
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }
    
    public boolean isTwoFactorAuthEnabled() { return twoFactorAuthEnabled; }
    public void setTwoFactorAuthEnabled(boolean twoFactorAuthEnabled) { 
        this.twoFactorAuthEnabled = twoFactorAuthEnabled; 
    }
}
```

#### Step 2: Update AuthController

**File: `backend/src/main/java/com/jayanth/tradingplatform/controller/AuthController.java`** (Critical Changes)

```java
package com.jayanth.tradingplatform.controller;

import com.jayanth.tradingplatform.config.JwtProvider;
import com.jayanth.tradingplatform.model.TwoFactorOTP;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.UserRepository;
import com.jayanth.tradingplatform.request.LoginRequest;  // ADD
import com.jayanth.tradingplatform.request.SignupRequest; // ADD
import com.jayanth.tradingplatform.response.AuthResponse;
import com.jayanth.tradingplatform.service.CustomUserDetailsService;
import com.jayanth.tradingplatform.service.EmailService;
import com.jayanth.tradingplatform.service.TwoFactorOTPService;
import com.jayanth.tradingplatform.utils.OtpUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;  // ADD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost", "http://localhost:3000"}, 
             allowCredentials = "true")  // ADD CORS
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody SignupRequest request,  // CHANGE: Use SignupRequest DTO
            HttpServletResponse response) throws Exception {
        
        String email = request.getEmail();
        String password = request.getPassword();
        String fullName = request.getFullName();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new Exception("Email is already registered");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFullName(fullName);

        User savedUser = userRepository.save(newUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        // Set both cookie AND return in response body
        setCookie(response, jwt);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(true);
        authResponse.setMessage("Signup successful");
        authResponse.setJwt(jwt);  // CHANGE: Return JWT in body

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,  // CHANGE: Use LoginRequest DTO
            HttpServletResponse response) throws Exception {
        
        String username = request.getEmail();
        String password = request.getPassword();

        Authentication auth = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        // Set both cookie AND return in response body
        setCookie(response, jwt);

        User authUser = userRepository.findByEmail(username);

        if(authUser.getTwoFactorAuth() != null && authUser.getTwoFactorAuth().isEnabled()) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two Factor Auth is enabled");
            authResponse.setTwoFactorAuthEnabled(true);
            
            String otp = OtpUtils.generateOtp();
            TwoFactorOTP oldTwoFactorOTP = twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwoFactorOTP != null) {
                twoFactorOTPService.deleteTwoFactorOTP(oldTwoFactorOTP);
            }
            
            TwoFactorOTP newTwoFactorOTP = twoFactorOTPService.createTwoFactorOTP(authUser, otp, jwt);
            emailService.sendVerificationOTPEmail(username, otp);
            authResponse.setSession(newTwoFactorOTP.getId());
            authResponse.setJwt(jwt);  // CHANGE: Also return JWT for 2FA flow
            
            return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(true);
        authResponse.setMessage("Login successful");
        authResponse.setJwt(jwt);  // CHANGE: Return JWT in body

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    // Helper method to set JWT cookie
    private void setCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600); // 1 hour
        // jwtCookie.setDomain("localhost"); // REMOVE this - causes production issues
        // Set SameSite attribute using header instead
        response.addHeader("Set-Cookie", "jwt=" + jwt + 
            "; HttpOnly; Secure; Path=/; SameSite=Strict; Max-Age=3600");
        response.addCookie(jwtCookie);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/twofactor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(
            @PathVariable String otp, 
            @RequestParam String id,
            HttpServletResponse response) throws Exception {  // ADD response param
        
        TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);
        
        if(twoFactorOTPService.verifyTwoFactorOTP(twoFactorOTP, otp)) {
            String jwt = twoFactorOTP.getJwt();
            setCookie(response, jwt);  // Also set cookie here
            
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor Auth verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(jwt);
            res.setStatus(true);
            
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        
        throw new Exception("Invalid OTP");
    }
}
```

### Frontend Changes

#### Step 1: Update axios interceptor

**File: `frontend/src/utils/axios.ts`**
```typescript
import axios, { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/',  // Use env variable
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,  // Send cookies
});

// Add a request interceptor
axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // CHANGE: Read from localStorage instead of cookies
    const token = localStorage.getItem('authToken');

    if (token && config.headers && !config.headers.Authorization) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor
axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    if (error.response && error.response.status === 401) {
      // Clear token and redirect to login
      localStorage.removeItem('authToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
```

#### Step 2: Update authService

**File: `frontend/src/services/authService.ts`**
```typescript
import axios from '../utils/axios';
import { AuthResponse, User } from '../types';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface SignupCredentials {
  fullName: string;
  email: string;
  password: string;
}

const authService = {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>('/auth/signin', credentials);
    
    // CHANGE: Store JWT from response body
    if (response.data.jwt) {
      localStorage.setItem('authToken', response.data.jwt);
    }
    
    return response.data;
  },

  async signup(credentials: SignupCredentials): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>('/auth/signup', credentials);
    
    // CHANGE: Store JWT from response body
    if (response.data.jwt) {
      localStorage.setItem('authToken', response.data.jwt);
    }
    
    return response.data;
  },

  async verify2FA(otp: string, sessionId: string): Promise<AuthResponse> {
    const response = await axios.post<AuthResponse>(
      `/auth/twofactor/otp/${otp}?id=${sessionId}`
    );
    
    // CHANGE: Store JWT from response body
    if (response.data.jwt) {
      localStorage.setItem('authToken', response.data.jwt);
    }
    
    return response.data;
  },

  async getCurrentUser(): Promise<User> {
    const response = await axios.get<User>('/api/users/profile');
    return response.data;
  },

  async logout(): Promise<void> {
    // CHANGE: Remove from localStorage instead of trying to clear HttpOnly cookie
    localStorage.removeItem('authToken');
    
    // Optionally call backend logout endpoint if you create one
    try {
      await axios.post('/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    }
    
    window.location.href = '/login';
  }
};

export default authService;
```

#### Step 3: Update authStore

**File: `frontend/src/store/authStore.ts`** (Only showing the login method, others similar)
```typescript
// ... imports ...

const useAuthStore = create<AuthState>((set, get) => ({
  // ... state ...

  login: async (credentials) => {
    set({ isLoading: true, error: null });
    try {
      const response = await authService.login(credentials);
      
      if (response.twoFactorAuthEnabled) {
        // CHANGE: Redirect to 2FA page will happen in LoginPage component
        set({ 
          twoFactorRequired: true, 
          sessionId: response.session || null,
          isLoading: false 
        });
      } else {
        set({ 
          isAuthenticated: true, 
          twoFactorRequired: false,
          isLoading: false
        });
        get().fetchCurrentUser();
      }
    } catch (error) {
      set({ 
        error: error instanceof Error ? error.message : 'Failed to login', 
        isLoading: false 
      });
    }
  },

  // ... rest of the store ...
}));

export default useAuthStore;
```

#### Step 4: Update LoginPage Component

**File: `frontend/src/features/auth/Login.tsx`** (Key changes)
```typescript
import { useNavigate } from 'react-router-dom';
import useAuthStore from '../../store/authStore';
// ... other imports ...

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { login, twoFactorRequired } = useAuthStore();
  
  // ... form setup ...
  
  const onSubmit = async (data: LoginCredentials) => {
    await login(data);
    
    // CHANGE: Check if 2FA is required after login
    if (twoFactorRequired) {
      navigate('/two-factor-auth');  // Redirect to 2FA page
    } else {
      navigate('/dashboard');  // Redirect to dashboard
    }
  };
  
  // ... rest of component ...
};

export default Login;
```

#### Step 5: Create .env files

**File: `frontend/.env.development`**
```
VITE_API_URL=http://localhost:8080
VITE_APP_NAME=Trading Platform Dev
```

**File: `frontend/.env.production`**
```
VITE_API_URL=https://api.yourdomain.com
VITE_APP_NAME=Trading Platform
```

---

## 2. ADD CORS CONFIGURATION

**File: `backend/src/main/java/com/jayanth/tradingplatform/config/SecurityConfig.java`** (Create if doesn't exist)

```java
package com.jayanth.tradingplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())  // Disable for REST API
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            );
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configure allowed origins based on environment
        String allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
        if (allowedOrigins != null) {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        } else {
            // Default for development
            configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost",
                "http://localhost:3000",
                "http://localhost:5173",
                "http://127.0.0.1"
            ));
        }
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 3. CREATE MISSING ENDPOINTS EXAMPLE

### Create User Controller

**File: `backend/src/main/java/com/jayanth/tradingplatform/controller/UserController.java`** (Create)

```java
package com.jayanth.tradingplatform.controller;

import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.UserRepository;
import com.jayanth.tradingplatform.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new UserResponse(user));
    }
}
```

**File: `backend/src/main/java/com/jayanth/tradingplatform/response/UserResponse.java`** (Create)

```java
package com.jayanth.tradingplatform.response;

import com.jayanth.tradingplatform.model.User;

public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    
    public UserResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
    
    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
```

---

## 4. FIX 2FA FLOW

**File: `backend/src/main/java/com/jayanth/tradingplatform/model/TwoFactorOTP.java`** (Update to add expiration)

```java
// Add these fields to TwoFactorOTP model:
private LocalDateTime createdAt;
private LocalDateTime expiresAt;  // 5 minutes after creation
private int attempts = 0;
private static final int MAX_ATTEMPTS = 3;

// Add method to check if expired
public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
}

// Add method to check if attempts exceeded
public boolean isMaxAttemptsExceeded() {
    return attempts >= MAX_ATTEMPTS;
}

// Add method to increment attempts
public void incrementAttempts() {
    this.attempts++;
}
```

**File: `backend/src/main/java/com/jayanth/tradingplatform/service/TwoFactorOTPService.java`** (Update verify method)

```java
public boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp) {
    // Check if OTP is expired
    if (twoFactorOTP.isExpired()) {
        return false;  // OTP expired
    }
    
    // Check if max attempts exceeded
    if (twoFactorOTP.isMaxAttemptsExceeded()) {
        return false;  // Too many attempts
    }
    
    // Check if OTP matches
    if (twoFactorOTP.getOtp().equals(otp)) {
        return true;  // Valid OTP
    }
    
    // Increment failed attempts
    twoFactorOTP.incrementAttempts();
    save(twoFactorOTP);
    return false;  // Invalid OTP
}
```

---

## Summary of Changes

| Component | Change | Impact |
|-----------|--------|--------|
| Backend | Return JWT in response body | ✓ Frontend can store token |
| Backend | Add CORS config | ✓ Requests work from frontend |
| Backend | Create DTO classes | ✓ Input validation works |
| Frontend | Read JWT from localStorage | ✓ axios interceptor sends auth |
| Frontend | Update auth service | ✓ Token stored properly |
| Frontend | Add 2FA redirect logic | ✓ 2FA flow works |
| Both | Fix 2FA expiration | ✓ Security improved |

---

## Testing Checklist

After making these changes, test:

- [ ] Signup creates user and returns JWT in response
- [ ] JWT is stored in localStorage
- [ ] Login returns JWT in response
- [ ] POST to /api/users/profile works and returns user data
- [ ] CORS allows requests from frontend
- [ ] 2FA flow redirects to /two-factor-auth when enabled
- [ ] OTP verification stores JWT and redirects to dashboard
- [ ] Logout clears token from localStorage
- [ ] 401 error redirects to login page

