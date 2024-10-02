package com.umc.ttg.domain.coupon.repository;

import com.umc.ttg.domain.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    List<CouponEntity> findAllByMemberId(Long memberId);

    Optional<CouponEntity> findByStoreId(Long storeId);

    Optional<CouponEntity> findByIdAndMemberId(Long couponId, Long memberId);

    Optional<CouponEntity> findByMemberIdAndStoreId(Long memberId, Long storeId);

    List<CouponEntity> findAllByMemberIdAndStoreId(Long memberId, Long storeId);

}
