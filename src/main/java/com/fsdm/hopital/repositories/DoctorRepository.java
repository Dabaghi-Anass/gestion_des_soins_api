package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into doctors (id) values (?1)", nativeQuery = true)
    void saveWithId(Long id);
    @Query("SELECT d FROM Doctor d JOIN Appointment a ON d.id = a.assignedTo.id " +
            "GROUP BY d.id ORDER BY COUNT(a.id) DESC")
    List<Doctor> findMostRated();
}
