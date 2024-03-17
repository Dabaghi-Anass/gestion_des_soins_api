package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long>{
    public Optional<EmailVerificationToken> findByToken(String token);
}
