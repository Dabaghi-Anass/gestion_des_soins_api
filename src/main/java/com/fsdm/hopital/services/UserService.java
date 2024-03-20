package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.User;
import com.fsdm.hopital.exceptions.UserNotSavedException;
import com.fsdm.hopital.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @SneakyThrows
    public User createUser(User user){
        if(user == null) throw new UserNotSavedException("invalid user details");
        Optional<User> userFromRepo = userRepository.findByUsername(user.getUsername());
        if(userFromRepo.isPresent()) throw new UserNotSavedException("user with this email already exists");
        return userRepository.save(user);
    }
    @SneakyThrows
    public User saveUser(User user){
        if(user == null) throw new UserNotSavedException("invalid user details");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
