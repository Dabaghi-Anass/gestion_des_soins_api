package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.*;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.CareGiverRepository;
import com.fsdm.hopital.repositories.PasswordRecoveryTokenRepository;
import com.fsdm.hopital.repositories.PatientRepository;
import com.fsdm.hopital.repositories.UserRepository;
import com.fsdm.hopital.dto.ChangePasswordRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final CareGiverRepository careGiverRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    @SneakyThrows
    public User createUser(User user){
        if(user == null) throw new AppException(ProcessingException.INVALID_USER_DETAILS);
        Optional<User> userFromRepo = userRepository.findByUsername(user.getUsername());
        if(userFromRepo.isPresent()) throw new AppException(ProcessingException.EMAIL_ALREADY_EXISTS);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setProfile(new Profile());
        return userRepository.save(user);
    }
    public boolean isSet(Object obj){
        return obj != null;
    }
    public User veriyUser(User user){
         user.setIsVerified(true);
        return userRepository.save(user);
    }
    @SneakyThrows
    public Patient getPatientById(Long id){
        return patientRepository.findById(id).orElseThrow(() -> new AppException("Patient not found"));
    }
    @SneakyThrows
    public User updateUser(User user){
        Optional<User> userOptional = userRepository.findById(user.getId());
        if(userOptional.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        User user1 = userOptional.get();
        user1.setId(user.getId());
        if(isSet(user.getRole())) user1.setRole(user.getRole());
        if(isSet(user.getUsername())) user1.setUsername(user.getUsername());
        if(isSet(user.getFirstName())) user1.setFirstName(user.getFirstName());
        if(isSet(user.getLastName())) user1.setLastName(user.getLastName());
        System.out.println("profile from request : " +user.getProfile());
        System.out.println("profile from response : " +user1.getProfile());
        if(isSet(user.getProfile())) user1.setProfile(user.getProfile());
        if(isSet(user.getIsVerified())) user1.setIsVerified(user.getIsVerified());
        if(user.getPassword() != null){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user1.setPassword(encoder.encode(user.getPassword()));
        }
        return userRepository.save(user1);
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
        User user = recoveryToken.get().getUser();
        if(user == null) throw new AppException(ProcessingException.USER_NOT_FOUND);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }
    @SneakyThrows
    @Transactional
    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        return user.get();
    }

    @SneakyThrows
    public CareGiver getCareGiverById(Long careGiverId) {
        Optional<CareGiver> careGiverOptional = careGiverRepository.findById(careGiverId);
        if(careGiverOptional.isEmpty()) throw new AppException(ProcessingException.USER_NOT_FOUND);
        return careGiverOptional.get();
    }
}
