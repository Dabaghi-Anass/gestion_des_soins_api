package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long>{
    Optional<PasswordRecoveryToken> findByToken(String token);
    void deleteByToken(String token);
}
