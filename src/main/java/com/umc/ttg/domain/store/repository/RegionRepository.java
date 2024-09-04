package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findById(Long id);
}
