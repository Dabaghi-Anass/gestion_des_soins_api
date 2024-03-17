package com.fsdm.hopital.controllers;

import com.fsdm.hopital.auth.jwt.JwtUtils;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.services.AuthService;
import com.fsdm.hopital.services.EmailService;
import com.fsdm.hopital.types.ActionEntity;
import com.fsdm.hopital.types.JsonWebToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<ActionEntity> registerUser(@RequestBody User userDetails) {
        authService.sendVerificationToken(userDetails);
        ActionEntity actionEntity = new ActionEntity("EMAIL_SENT" , "verification email sent" , true);
        return ResponseEntity.ok(actionEntity);
    }
    @GetMapping("/verifyEmail")
    public ResponseEntity<JsonWebToken> verifyAndRegisterUser(@RequestParam("token") String token) {
        User user =  authService.verifyEmail(token);
        String jwt = jwtUtils.generateToken(user);
        return ResponseEntity.ok().body(new JsonWebToken(jwt));
    }
}
