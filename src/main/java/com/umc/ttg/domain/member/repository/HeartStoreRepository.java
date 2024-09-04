package com.umc.ttg.domain.member.repository;

import com.umc.ttg.domain.member.entity.HeartStore;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartStoreRepository extends JpaRepository<HeartStore, Long> {

    Optional<HeartStore> findByMemberAndStore(Member member, Store store);
    List<HeartStore> findByMember(Member member);

}
