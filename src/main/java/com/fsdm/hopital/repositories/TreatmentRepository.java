package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Treatment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    @Query(nativeQuery = true,value = "SELECT * FROM treatments WHERE sent_by_id = ?1 OR sent_to_id = ?1")
    List<Treatment> findAllByUserId(Long id);
    @Query(nativeQuery = true,value = "SELECT * FROM treatments WHERE sent_by_id = ?1 OR sent_to_id = ?1")
    List<Treatment> findAllByUserId(Long id, Pageable pageable);
}
