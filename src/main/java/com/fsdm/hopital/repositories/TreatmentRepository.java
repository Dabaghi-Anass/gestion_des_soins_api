package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Treatment;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    @Query("SELECT t FROM Treatment t WHERE t.sentBy.id=?1")
    List<Treatment> findAllByUserId(Long id);
}
