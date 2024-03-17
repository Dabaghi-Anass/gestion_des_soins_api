package com.fsdm.hopital.auth.jwt;

import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.types.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Payload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@Getter
@RequiredArgsConstructor
public class JwtUtils {
    private String signingKey = "Ixk44iWJj6ZSRUZ2b6Oc1KH0YzsoEqsjR21x8WEUUzyU8xlhjOn9bwcfhjsykbAuTJEUaAtqvxRvi2ORZa54aIQCuIb7oVJgu3aWPFsiVYzXZfLOZJs9Du2RXp96JwnN";
    private int expiration = 604_800;
    public String generateToken(User user) {
        User userFromAuth = (User) user;
        String jwt = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", userFromAuth.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256 , signingKey.getBytes())
                .compact();
        return jwt;
    }
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
        Role role = Role.valueOf((String) extractClaim(token, claims -> claims.get("role")));
        return role;
    }
    public String extractUserName(String token) {
        return extractClaim(token , Claims::getSubject).toString();
    }
    public Date getTokenExpiration(String token){
       return  (Date) extractClaim(token , Claims::getExpiration);
    }
    public boolean isTokenExpired(String token) {
        Date expirationDate = getTokenExpiration(token);
        return expirationDate.before(new Date());
    }
}
