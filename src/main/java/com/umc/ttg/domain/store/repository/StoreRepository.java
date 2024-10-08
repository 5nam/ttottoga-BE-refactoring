package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.MenuEntity;
import com.umc.ttg.domain.store.entity.RegionEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    List<StoreEntity> findByRegion(RegionEntity regionEntity);
    List<StoreEntity> findByTitleContainingOrNameContaining(String keyword, String name);
    List<StoreEntity> findByMenu(MenuEntity menuEntity);

}
