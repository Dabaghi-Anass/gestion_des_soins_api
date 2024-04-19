package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.TreatmentRequest;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRequestRepository extends JpaRepository<TreatmentRequest, Long> {
    @Query("SELECT t from TreatmentRequest t where t.sentTo.id = ?1")
    List<TreatmentRequest> findAllBySentToId(Long id);
}
