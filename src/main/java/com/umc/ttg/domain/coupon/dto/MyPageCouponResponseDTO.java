package com.umc.ttg.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageCouponResponseDTO {

    private Long id;
    private String content;
}
