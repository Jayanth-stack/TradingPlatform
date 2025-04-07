package com.jayanth.tradingplatform.config;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {

    private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(Authentication auth) {
        String roles = populateAuthorities(auth.getAuthorities());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("email", auth.getName())
                .claim("Authorities", roles)
                .signWith(key)
                .compact();
    }

    public static String getEmailFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                    
            return String.valueOf(claims.get("email"));
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            throw new RuntimeException("Invalid token: " + e.getMessage(), e);
        }
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}