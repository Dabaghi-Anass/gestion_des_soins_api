package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Doctor;
import com.fsdm.hopital.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);
    @Query("select e from User e where e.role = 'DOCTOR' or e.role = 'CAREGIVER' or e.role = 'NURSE'")
    List<User> findAllEmployees();
}
