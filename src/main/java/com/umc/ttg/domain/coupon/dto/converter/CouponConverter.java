package com.umc.ttg.domain.coupon.dto.converter;

import com.umc.ttg.domain.coupon.dto.MyPageCouponResponseDTO;
import com.umc.ttg.domain.coupon.entity.CouponEntity;

public class CouponConverter {

    public static MyPageCouponResponseDTO convertToMyCouponDto(CouponEntity couponEntity) {
        if (couponEntity == null) {
            return null;
        }

        return MyPageCouponResponseDTO.builder()
                .id(couponEntity.getId())
                .content(couponEntity.getContent())
                .build();
    }

    public static String convertToCouponUsage(CouponEntity couponEntity) {

        return couponEntity.getId() + "번 쿠폰 사용 완료";
    }
}
