package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
//    Optional<RegionEntity> findById(Long id);
}
