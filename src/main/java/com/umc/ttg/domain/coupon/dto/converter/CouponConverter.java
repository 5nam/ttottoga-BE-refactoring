package com.umc.ttg.domain.coupon.dto.converter;

import com.umc.ttg.domain.coupon.dto.MyPageCouponResponseDTO;
import com.umc.ttg.domain.coupon.entity.Coupon;

public class CouponConverter {

    public static MyPageCouponResponseDTO convertToMyCouponDto(Coupon coupon) {
        if (coupon == null) {
            return null;
        }

        return MyPageCouponResponseDTO.builder()
                .id(coupon.getId())
                .content(coupon.getContent())
                .build();
    }

    public static String convertToCouponUsage(Coupon coupon) {

        return coupon.getId() + "번 쿠폰 사용 완료";
    }
}
