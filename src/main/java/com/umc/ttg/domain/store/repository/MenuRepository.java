package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
