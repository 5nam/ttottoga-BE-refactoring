package com.umc.ttg.domain.review.dto;

import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.review.entity.Review;
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

    public static MyPageReviewResponseDTO of(Long memberId, Review review, CouponRepository couponRepository) {
        MyPageStoreResponseDto myPageStoreResponseDto =
                StoreConverter.convertToMyStoreDto(memberId, review.getStore(), couponRepository);

        return MyPageReviewResponseDTO.builder()
                .reviewId(review.getId())
                .reviewLink(review.getReviewLink())
                .status(review.getStatus())
                .applyDate(review.getApplyDate())
                .reason(review.getReason())
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
