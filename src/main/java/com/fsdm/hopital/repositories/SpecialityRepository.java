package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.Speciality;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality,Long> {
    @Query("SELECT s.category FROM Speciality s")
    List<String> findAllCategories();
}
