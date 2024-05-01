package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Nurse;
import com.fsdm.hopital.entities.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into nurses (id) values (?1)", nativeQuery = true)
    void saveWithId(Long id);
}
