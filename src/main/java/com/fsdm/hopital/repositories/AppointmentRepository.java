package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByAssignedToId(Long id);
    List<Appointment> findAllByPatientId(Long id);
    @Query("SELECT a FROM Appointment a WHERE a.assignedTo.id = ?1 OR a.patient.id = ?1")
    List<Appointment> findAllByUserId(Long id);
    @Transactional
    @Modifying
    @Query("delete from Appointment a where a.assignedTo.id = ?1 OR a.patient.id = ?1")
    void deleteAllByUserId(Long id);
}
