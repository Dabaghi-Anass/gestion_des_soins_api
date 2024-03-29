package com.fsdm.hopital.controllers;

import com.fsdm.hopital.auth.jwt.JwtUtils;
import com.fsdm.hopital.entities.Profile;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.AuthService;
import com.fsdm.hopital.services.ProfileService;
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
    private final ProfileService profileService;
    public record RegisterResponse(ActionEntity action , User user){}
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Validated @RequestBody User userDetails) {
        User user = authService.sendVerificationToken(userDetails);
        ActionEntity actionEntity = new ActionEntity("EMAIL_SENT" , "verification email sent" , true);
        String jwt = jwtUtils.generateToken(userDetails);
        response.addCookie(createAuthCookie(jwt));
        response.addHeader("x-auth" , jwt);
        user.setPassword(null);
        return ResponseEntity.ok(new RegisterResponse(actionEntity , user));
    }
    private Cookie createAuthCookie(String token){
        Cookie cookie = new Cookie("x-auth" , token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)new Date(jwtUtils.getExpiration() - 100).getTime());
        cookie.setPath("/");
        return cookie;
    }
    @PutMapping("/user/createWithProfile")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        user1.setPassword(null);
        return ResponseEntity.ok(user1);
    }
    @GetMapping("/isVerified")
    public ResponseEntity<ActionEntity> isEmailVerified(@RequestHeader("x-auth") String token) {
        boolean valid = jwtUtils.validateTokenSignature(token);
        if(!valid) return ResponseEntity.badRequest().body(new ActionEntity("INVALID_TOKEN" , "invalid auth token" , false));
        String username = jwtUtils.extractUserName(token);
        User user = userService.getUserByUsername(username);
        if(user == null) return ResponseEntity.badRequest().body(new ActionEntity("USER_NOT_FOUND" , "user not found" , false));
        return ResponseEntity.ok(new ActionEntity("USER_VERIFIED" , "user verified successfully" , user.getIsVerified()));
    }
    @GetMapping("/verifyEmail")
    public String verifyAndRegisterUser(@RequestParam("token") String token) {
        User user =  authService.verifyEmail(token);
        String jwt = jwtUtils.generateToken(user);
        response.addCookie(createAuthCookie(jwt));
        response.addHeader("x-auth" , jwt);
        return "email verified successfully";
    }
    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("x-auth") String token) {
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
        response.addHeader("x-auth" , jwt);
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
