package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Companion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanionRepository extends JpaRepository<Companion, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into companions (id) values (?1)", nativeQuery = true)
    void saveWithId(Long id);
}
