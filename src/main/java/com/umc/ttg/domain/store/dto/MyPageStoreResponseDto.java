package com.umc.ttg.domain.store.dto;

import com.umc.ttg.domain.coupon.dto.MyPageCouponResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageStoreResponseDto {

    private Long storeId;
    private String title;
    private String name;
    private String image;
    private MyPageCouponResponseDTO couponDto;
}
