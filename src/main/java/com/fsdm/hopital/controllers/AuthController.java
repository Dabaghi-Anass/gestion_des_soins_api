package com.fsdm.hopital.controllers;

import com.fsdm.hopital.auth.jwt.JwtUtils;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.AuthService;
import com.fsdm.hopital.types.ActionEntity;
import com.fsdm.hopital.types.JsonWebToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final HttpServletResponse response;
    @PostMapping("/register")
    public ResponseEntity<ActionEntity> registerUser(@Valid  @RequestBody User userDetails) {
        authService.sendVerificationToken(userDetails);
        ActionEntity actionEntity = new ActionEntity("EMAIL_SENT" , "verification email sent" , true);
        return ResponseEntity.ok(actionEntity);
    }
    private Cookie createAuthCookie(String token){
        Cookie cookie = new Cookie("x-auth" , token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)new Date(jwtUtils.getExpiration()).getTime());
        cookie.setPath("/");
        return cookie;
    }
    @GetMapping("/verifyEmail")
    public ResponseEntity<JsonWebToken> verifyAndRegisterUser(@RequestParam("token") String token) {
        User user =  authService.verifyEmail(token);
        String jwt = jwtUtils.generateToken(user);
        Cookie cookie = createAuthCookie(jwt);
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new JsonWebToken(jwt));
    }
    @GetMapping("/currentuser")
    public ResponseEntity<User> getCurrentUser(@CookieValue("x-auth") String token) {
        boolean valid = jwtUtils.validateTokenSignature(token);
        if(!valid) return ResponseEntity.badRequest().build();
        String username = jwtUtils.extractUserName(token);
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}
