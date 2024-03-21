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
        User userFromDb = userService.createUser(user);
        EmailVerificationToken tokenFromDb = tokenService.saveToken(generateEmailVToken(userFromDb));
        emailService.sendVerificationEmail(user.getUsername(), user.getFirstName(), tokenFromDb.getToken());
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
    public User loginUser(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        if(userFromDb.isEmpty()) throw new UserNotFoundException("invalid username or password");
        User user1 = userFromDb.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(user.getPassword(), user1.getPassword())) return user1;
        else{
            throw new UserNotFoundException("invalid username or password");
        }
    }
}
