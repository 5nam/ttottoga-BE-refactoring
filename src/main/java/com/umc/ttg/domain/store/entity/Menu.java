package com.umc.ttg.domain.store.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

}
