package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.EmailVerificationToken;
import com.fsdm.hopital.entities.PasswordRecoveryToken;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.PasswordRecoveryTokenRepository;
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
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
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
    public void sendPasswordRetrieveLink(User user){
        String token = UUID.randomUUID().toString();
        PasswordRecoveryToken tokenE = new PasswordRecoveryToken();
        tokenE.setToken(token);
        tokenE.setUsername(user.getUsername());
        PasswordRecoveryToken tokenFromDb = passwordRecoveryTokenRepository.save(tokenE);
        String link = emailService.getServerLink() + "/api/auth/reset-password?token="+tokenFromDb.getToken();
        String body = "Bonjour Mr (Mlle) " + user.getFirstName() + "\n" +
                "clicker le lien si dessus pour reinitialiser votre mot de passe\n" +
                link+
                "\nsi vous ne vous êtes pas inscrit sur notre application, veuillez\n" +
                "ne pas cliquer sur ce lien\n";
        emailService.sendEmail(user.getUsername(), "Password Reset", user.getFirstName(), body);
    }
    @SneakyThrows
    public User verifyEmail(String token) {
        EmailVerificationToken tokenFromDb = tokenService.findByToken(token);
        User user = tokenFromDb.getUser();
        user.setIsVerified(true);
        User savedUser = userService.saveUser(user);
        if(savedUser == null) throw new AppException(ProcessingException.USER_NOT_SAVED);
        tokenService.deleteToken(tokenFromDb);
        return savedUser;
    }

    @SneakyThrows
    public User loginUser(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        if(userFromDb.isEmpty()) throw new AppException(ProcessingException.INVALID_USERNAME_PASSWORD);
        User user1 = userFromDb.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(user.getPassword(), user1.getPassword())) return user1;
        else{
            throw new AppException(ProcessingException.INVALID_USERNAME_PASSWORD);
        }
    }
}
