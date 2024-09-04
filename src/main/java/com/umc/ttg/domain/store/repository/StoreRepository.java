package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.Menu;
import com.umc.ttg.domain.store.entity.Region;
import com.umc.ttg.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByRegion(Region region);
    List<Store> findByTitleContainingOrNameContaining(String keyword, String name);
    List<Store> findByMenu(Menu menu);

}
