package com.umc.ttg.domain.store.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "REGION")
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    private Long upperId;

    @Builder
    public RegionEntity(String name) {
        this.name = name;
    }
}
