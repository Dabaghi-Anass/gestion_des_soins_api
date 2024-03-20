package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.EmailVerificationToken;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.UserNotFoundException;
import com.fsdm.hopital.exceptions.UserNotSavedException;
import com.fsdm.hopital.repositories.EmailVerificationTokenRepository;
import com.fsdm.hopital.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailVerificationTokenService tokenService;
    private final UserService userService;
    private EmailVerificationToken generateEmailVToken(User user){
        String token = UUID.randomUUID().toString();
        return EmailVerificationToken
                .builder()
                .user(user)
                .token(token)
                .build();
    }
    @SneakyThrows
    public void sendVerificationToken(User user) {
        user.setIsVerified(false);
        User userFromDb = userService.saveUser(user);
        EmailVerificationToken tokenFromDb = tokenService.saveToken(generateEmailVToken(userFromDb));
        emailService.sendVerificationEmail(user.getUsername(), user.getFirstName(), tokenFromDb.getToken());
    }
    public void loginUser(User user) {
        System.out.println("User logged in");
    }

    @SneakyThrows
    public User verifyEmail(String token) {
        EmailVerificationToken tokenFromDb = tokenService.findByToken(token);
        User user = tokenFromDb.getUser();
        user.setIsVerified(true);
        User savedUser = userService.saveUser(user);
        if(savedUser == null) throw new UserNotSavedException("User not saved");
        tokenService.deleteToken(tokenFromDb);
        return savedUser;
    }
    @SneakyThrows
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new UserNotFoundException("User not found");
        return user.get();
    }
}
