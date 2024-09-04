package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
