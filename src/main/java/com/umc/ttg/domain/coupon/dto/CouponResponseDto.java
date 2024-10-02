package com.umc.ttg.domain.coupon.dto;

import com.umc.ttg.domain.coupon.entity.CouponEntity;
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

    public static CouponResponseDto of(CouponEntity couponEntity) {
        return CouponResponseDto.builder()
                .id(couponEntity.getId())
                .name(couponEntity.getName())
                .subtitle(couponEntity.getContent())
                .useYn(couponEntity.getStatusYn())
                .qrCode(couponEntity.getQrCode())
                .storeImage(couponEntity.getImageUrl())
                .startDate(couponEntity.getStartDate())
                .endDate(couponEntity.getEndDate())
                .build();
    }

}
