package com.umc.ttg.domain.review.dto;

import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.review.entity.ReviewEntity;
import com.umc.ttg.domain.review.entity.ReviewStatus;
import com.umc.ttg.domain.store.dto.MyPageStoreResponseDto;
import com.umc.ttg.domain.store.dto.converter.StoreConverter;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MyPageReviewResponseDTO {

    private Long reviewId;
    private String reviewLink;
    private ReviewStatus status;
    private LocalDate applyDate;
    private String reason;
    private MyPageStoreResponseDto storeDto;

    public static MyPageReviewResponseDTO of(Long memberId, ReviewEntity reviewEntity, CouponRepository couponRepository) {
        MyPageStoreResponseDto myPageStoreResponseDto =
                StoreConverter.convertToMyStoreDto(memberId, reviewEntity.getStore(), couponRepository);

        return MyPageReviewResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .reviewLink(reviewEntity.getReviewLink())
                .status(reviewEntity.getStatus())
                .applyDate(reviewEntity.getApplyDate())
                .reason(reviewEntity.getReason())
                .storeDto(myPageStoreResponseDto)
                .build();
    }

    @Builder
    private MyPageReviewResponseDTO(Long reviewId, String reviewLink, ReviewStatus status, LocalDate applyDate, String reason, MyPageStoreResponseDto storeDto) {
        this.reviewId = reviewId;
        this.reviewLink = reviewLink;
        this.status = status;
        this.applyDate = applyDate;
        this.reason = reason;
        this.storeDto = storeDto;
    }
}
