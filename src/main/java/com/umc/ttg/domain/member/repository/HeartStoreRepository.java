package com.umc.ttg.domain.member.repository;

import com.umc.ttg.domain.member.entity.HeartStoreEntity;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartStoreRepository extends JpaRepository<HeartStoreEntity, Long> {

    Optional<HeartStoreEntity> findByMemberAndStore(MemberEntity memberEntity, StoreEntity storeEntity);
    List<HeartStoreEntity> findByMember(MemberEntity memberEntity);

}
