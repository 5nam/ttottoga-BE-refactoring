package com.umc.ttg.domain.review.repository;

import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.review.entity.ReviewEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByStoreAndMember(StoreEntity storeEntity, MemberEntity memberEntity);

    List<ReviewEntity> findAllByMemberId(Long memberId);

    List<ReviewEntity> findAllByMemberName(String memberName);

    Optional<ReviewEntity> findByStoreIdAndMemberId(Long storeId, Long memberId);
}
