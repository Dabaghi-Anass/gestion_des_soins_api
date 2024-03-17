package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.EmailVerificationToken;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.UserNotSavedException;
import com.fsdm.hopital.repositories.EmailVerificationTokenRepository;
import com.fsdm.hopital.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    @SneakyThrows
    public void sendVerificationToken(User user) {
        user.setIsVerified(false);
        User userFromDb = userRepository.save(user);
        if(userFromDb == null) {
            throw new UserNotSavedException("User not saved");
        }
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken
                .builder()
                .user(userFromDb)
                .token(token)
                .build();
        EmailVerificationToken tokenFromDb = tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getUsername(), user.getFirstName(), tokenFromDb.getToken());
    }
    public void loginUser(User user) {
        System.out.println("User logged in");
    }
    public User verifyEmail(String token) {
        Optional<EmailVerificationToken> tokenFromDb = tokenRepository.findByToken(token);
        System.out.println(tokenFromDb);
        if (tokenFromDb.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        User user = tokenFromDb.get().getUser();
        user.setIsVerified(true);
        return userRepository.save(user);
    }
}
