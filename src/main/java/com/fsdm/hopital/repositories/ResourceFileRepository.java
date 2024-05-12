package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.ResourceFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long>{
    ResourceFile findByName(String name);
    ResourceFile findByUrl(String url);

    @Query("select r from ResourceFile r where r.owner.id = ?1 or r.creator.id = ?1")
    List<ResourceFile> findAllByUserId(Long userId);
    @Query("select r from ResourceFile r where r.owner.id = ?1 or r.creator.id = ?1")
    List<ResourceFile> findAllByUserId(Long userId, Pageable page);
}
