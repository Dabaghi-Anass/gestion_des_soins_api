package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.PasswordRecoveryToken;
import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.PasswordRecoveryTokenRepository;
import com.fsdm.hopital.repositories.UserRepository;
import com.fsdm.hopital.types.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    @SneakyThrows
    public User createUser(User user){
        if(user == null) throw new AppException(ProcessingException.INVALID_USER_DETAILS);
        Optional<User> userFromRepo = userRepository.findByUsername(user.getUsername());
        if(userFromRepo.isPresent()) throw new AppException(ProcessingException.EMAIL_ALREADY_EXISTS);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @SneakyThrows
    public User saveUser(User user){
        if(user == null) throw new AppException(ProcessingException.INVALID_USER_DETAILS);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @SneakyThrows
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new AppException(ProcessingException.INVALID_USER_DETAILS);
        return user.get();
    }

    @SneakyThrows
    public void changePassword(ChangePasswordRequest request){
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        User user1 = user.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(request.getOldPassword(), user1.getPassword())){
            user1.setPassword(encoder.encode(request.getNewPassword()));
            userRepository.save(user1);
        }else{
            throw new AppException(ProcessingException.INVALID_PASSWORD);
        }
    }

    @SneakyThrows
    public void deleteUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        userRepository.delete(user.get());
    }
    @SneakyThrows
    public void resetPassword(String token , String newPassword){
        Optional<PasswordRecoveryToken> recoveryToken = passwordRecoveryTokenRepository.findByToken(token);
        if(recoveryToken.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        Optional<User> user = userRepository.findByUsername(recoveryToken.get().getUsername());
        if(user.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        User user1 = user.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user1.setPassword(encoder.encode(newPassword));
        userRepository.save(user1);
    }
}
