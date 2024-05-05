package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Appointment;
import com.fsdm.hopital.entities.CareActivity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareActivityRepository extends JpaRepository<CareActivity, Long> {
    List<CareActivity> findAllByCaregiverId(Long id);
    List<CareActivity> findAllByPatientId(Long id);
    @Query("SELECT a FROM CareActivity a WHERE a.caregiver.id = ?1 OR a.patient.id = ?1")
    List<CareActivity> findAllByUserId(Long id);
    @Query("SELECT a FROM CareActivity a WHERE a.caregiver.id = ?1 OR a.patient.id = ?1")
    List<CareActivity> findAllByUserId(Long id, Pageable pageable);
    @Transactional
    @Modifying
    @Query("delete from CareActivity a where a.caregiver.id = ?1 OR a.patient.id = ?1")
    void deleteAllByUserId(Long id);
}
