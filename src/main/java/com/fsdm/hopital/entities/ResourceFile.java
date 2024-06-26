package com.fsdm.hopital.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceFile extends BaseEntity{
    String name;
    String originalName;
    String contentType;
    String url;
    double size;
    @ManyToOne
    User owner;
    @ManyToOne
    User creator;
}
