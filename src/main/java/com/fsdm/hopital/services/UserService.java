package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.PasswordRecoveryToken;
import com.fsdm.hopital.entities.User;
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
        if(user == null) throw new Exception("invalid user details");
        Optional<User> userFromRepo = userRepository.findByUsername(user.getUsername());
        if(userFromRepo.isPresent()) throw new Exception("user with this email already exists");
        return userRepository.save(user);
    }
    @SneakyThrows
    public User saveUser(User user){
        if(user == null) throw new Exception("invalid user details");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @SneakyThrows
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new Exception("User not found");
        return user.get();
    }

    @SneakyThrows
    public void changePassword(ChangePasswordRequest request){
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()) throw new Exception("User not found");
        User user1 = user.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(request.getOldPassword(), user1.getPassword())){
            user1.setPassword(encoder.encode(request.getNewPassword()));
            userRepository.save(user1);
        }else{
            throw new Exception("invalid password");
        }
    }

    @SneakyThrows
    public void deleteUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new Exception("User not found");
        userRepository.delete(user.get());
    }
    @SneakyThrows
    public void resetPassword(String token , String newPassword){
        Optional<PasswordRecoveryToken> recoveryToken = passwordRecoveryTokenRepository.findByToken(token);
        if(recoveryToken.isEmpty()) throw new Exception("User not found");
        Optional<User> user = userRepository.findByUsername(recoveryToken.get().getUsername());
        if(user.isEmpty()) throw new Exception("User not found");
        User user1 = user.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user1.setPassword(encoder.encode(newPassword));
        userRepository.save(user1);
    }
}
