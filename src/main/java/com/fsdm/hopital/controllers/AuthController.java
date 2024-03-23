package com.fsdm.hopital.controllers;

import com.fsdm.hopital.auth.jwt.JwtUtils;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.AuthService;
import com.fsdm.hopital.services.UserService;
import com.fsdm.hopital.types.ActionEntity;
import com.fsdm.hopital.types.ChangePasswordRequest;
import com.fsdm.hopital.types.JsonWebToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final HttpServletResponse response;
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<ActionEntity> registerUser(@Validated @RequestBody User userDetails) {
        authService.sendVerificationToken(userDetails);
        ActionEntity actionEntity = new ActionEntity("EMAIL_SENT" , "verification email sent" , true);
        return ResponseEntity.ok(actionEntity);
    }
    private Cookie createAuthCookie(String token){
        Cookie cookie = new Cookie("x-auth" , token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)new Date(jwtUtils.getExpiration() - 100).getTime());
        cookie.setPath("/");
        return cookie;
    }
    @GetMapping("/verifyEmail")
    public String verifyAndRegisterUser(@RequestParam("token") String token) {
        User user =  authService.verifyEmail(token);
        String jwt = jwtUtils.generateToken(user);
        Cookie cookie = createAuthCookie(jwt);
        response.addCookie(cookie);
        return "email verified successfully";
    }
    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(@CookieValue("x-auth") String token) {
        boolean valid = jwtUtils.validateTokenSignature(token);
        if(!valid) return ResponseEntity.badRequest().build();
        String username = jwtUtils.extractUserName(token);
        User user = userService.getUserByUsername(username);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        User loggedUser = authService.loginUser(user);
        loggedUser.setPassword(null);
        String jwt = jwtUtils.generateToken(loggedUser);
        Cookie cookie = createAuthCookie(jwt);
        response.addCookie(cookie);
        return ResponseEntity.ok().body(loggedUser);
    }
    @GetMapping("/logout")
    public void logoutUser() {
        Cookie cookie = new Cookie("x-auth" , "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    @PostMapping("/change-password")
    public ResponseEntity<ActionEntity> changePassword(@RequestBody ChangePasswordRequest changeRequest){
        userService.changePassword(changeRequest);
        return ResponseEntity.ok(new ActionEntity("PASSWORD_CHANGED" , "password changed successfully" , true));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ActionEntity> forgotPassword(@RequestBody String username){
        User user = userService.getUserByUsername(username);
        authService.sendPasswordRetrieveLink(user);
        return ResponseEntity.ok(new ActionEntity("EMAIL_SENT" , "password recovery email sent" , true));
    }
    @PostMapping("/reset-password/{token}")
    public ResponseEntity<ActionEntity> resetPassword(@PathVariable("token") String token , @RequestBody String newPassword){
        userService.resetPassword(token , newPassword);
        return ResponseEntity.ok(new ActionEntity("PASSWORD_RESET" , "password reset successfully" , true));
    }
}
