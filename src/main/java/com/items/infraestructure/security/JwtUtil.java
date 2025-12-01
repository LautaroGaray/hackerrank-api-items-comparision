package com.items.infraestructure.security;

import java.util.Date;
import java.security.Key;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.items.infraestructure.config.JwtProperties;
import com.items.domain.port.outbound.TokenProviderPort;

@Component
public class JwtUtil implements TokenProviderPort {
    private final Key key;
    private final long expirationMs;
    public JwtUtil(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes());
        this.expirationMs = props.getExpirationMs();
    }
    @Override
    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(exp).signWith(key).compact();
    }
    @Override
    public String validateAndGetSubject(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return claims.getBody().getSubject();
    }
}