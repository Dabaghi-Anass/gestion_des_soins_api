package com.fsdm.hopital.auth.jwt;

import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.UserService;
import com.fsdm.hopital.types.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@Getter
@RequiredArgsConstructor
public class JwtUtils {
    private final UserService userService;
    private String signingKey =  "qwertyuiopasdfghjklzxcvbnm123456";
    private int expiration = 604_800_000;
    public String generateToken(User user) {
        String jwt = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256 , signingKey.getBytes())
                .compact();
        return jwt;
    }
    @SneakyThrows
    public Claims extractPayload(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> Object extractClaim(String token , Function<Claims,T> resolver){
        Claims payload = extractPayload(token);
        return resolver.apply(payload);
    }
    public boolean validateToken(String token, User user) {
        String username = extractUserName(token);
        if(username == null || username.isEmpty())
            return false;
        return username.equals(user.getUsername());
    }
    public Role extractRole(String token) {
        return Role.valueOf((String) extractClaim(token, claims -> claims.get("role")));
    }
    public String extractUserName(String token) {
        return extractClaim(token , Claims::getSubject).toString();
    }
    public Date getTokenExpiration(String token){
        return  (Date) extractClaim(token , Claims::getExpiration);
    }
    public boolean isTokenExpired(String token) {
        return getTokenExpiration(token).before(new Date());
    }
    public boolean validateTokenSignature(String token){
        return Jwts.parser()
                .setSigningKey(signingKey.getBytes())
                .isSigned(token) && !isTokenExpired(token);
    }
    public User extractUserFromJwt(String token){
        String username = extractUserName(token);
        return userService.getUserByUsername(username);
    }
}
