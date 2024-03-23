package com.fsdm.hopital.services;

import com.fsdm.hopital.entities.EmailVerificationToken;
import com.fsdm.hopital.exceptions.AppException;
import com.fsdm.hopital.exceptions.ProcessingException;
import com.fsdm.hopital.repositories.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {
    private final EmailVerificationTokenRepository repository;
    public EmailVerificationToken saveToken(EmailVerificationToken token){
        return repository.save(token);
    }
    @SneakyThrows
    public  EmailVerificationToken findByToken(String token){
        Optional<EmailVerificationToken> tokenFromRepo = repository.findByToken(token);
        if(tokenFromRepo.isEmpty()) throw new AppException(ProcessingException.TOKEN_NOT_FOUND);
        return tokenFromRepo.get();
    }
    public void deleteToken(EmailVerificationToken token){
        repository.delete(token);
    }

}
