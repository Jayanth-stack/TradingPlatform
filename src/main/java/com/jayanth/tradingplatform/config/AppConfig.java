package com.jayanth.tradingplatform.config;


import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
public class AppConfig {
    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSession) throws Exception {
        httpSession.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(Authorize -> Authorize.requestMatchers("/api/**").authenticated().anyRequest
        ().permitAll())
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class).csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return httpSession.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {

        return null;
    }
}
