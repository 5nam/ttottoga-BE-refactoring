package com.umc.ttg.domain.coupon.dto;

import com.umc.ttg.domain.coupon.entity.Coupon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CouponResponseDto {
    private Long id;
    private String name;
    private String subtitle;
    private Character useYn;
    private String qrCode;
    private String storeImage;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public CouponResponseDto(Long id, String name, String subtitle, Character useYn, String qrCode, String storeImage, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.useYn = useYn;
        this.qrCode = qrCode;
        this.storeImage = storeImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static CouponResponseDto of(Coupon coupon) {
        return CouponResponseDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .subtitle(coupon.getContent())
                .useYn(coupon.getStatusYn())
                .qrCode(coupon.getQrCode())
                .storeImage(coupon.getImageUrl())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .build();
    }

}
