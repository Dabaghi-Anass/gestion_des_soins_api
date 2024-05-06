package com.fsdm.hopital.repositories;

import com.fsdm.hopital.entities.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long>{
    ResourceFile findByName(String name);
    ResourceFile findByUrl(String url);
}
