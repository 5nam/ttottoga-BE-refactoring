package com.umc.ttg.domain.store.dto.converter;

import com.umc.ttg.domain.coupon.dto.MyPageCouponResponseDTO;
import com.umc.ttg.domain.coupon.dto.converter.CouponConverter;
import com.umc.ttg.domain.coupon.entity.CouponEntity;
import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.review.entity.ReviewEntity;
import com.umc.ttg.domain.store.dto.MyPageStoreResponseDto;
import com.umc.ttg.domain.store.dto.StoreResponseDto;
import com.umc.ttg.domain.store.dto.StoreFindResponseDto;
import com.umc.ttg.domain.store.entity.StoreEntity;

import java.util.List;

public class StoreConverter {

    // Store 정보 -> StoreCreateResponseDto 로
    public static StoreResponseDto convertToStoreResponse(Long storeId) {

        return new StoreResponseDto(storeId);

    }

    // 리뷰 썸네일 -> 상점 이미지
    public static String convertToReviewImage(ReviewEntity reviewEntity) {

        return reviewEntity.getStore().getImage();

    }

    // 리뷰 작성자 이미지 -> Member 프로필 이미지
    public static String convertToReviewProfileImage(ReviewEntity reviewEntity) {

        return reviewEntity.getMember().getProfileImage();

    }

    // 리뷰 작성자 -> Member 닉네임
    public static String convertToReviewNickname(ReviewEntity reviewEntity) {

        return reviewEntity.getMember().getNickname();

    }

    // 리뷰 타이틀 -> {Member 닉네임}'님의 또또가 리뷰'
    public static String convertToReviewTitle(ReviewEntity reviewEntity) {

        return reviewEntity.getMember().getNickname() + " 님의 또또가 리뷰";

    }

    // Store 정보 -> StoreFindResponseDto 로
    public static StoreFindResponseDto convertToStoreFindResponseDto(StoreEntity storeEntity, boolean submitReview, boolean heartStore) {
        return StoreFindResponseDto.builder()
                .storeImage(storeEntity.getImage())
                .title(storeEntity.getTitle())
                .subTitle(storeEntity.getSubTitle())
                .regionName(storeEntity.getRegion().getName())
                .menuName(storeEntity.getMenu().getName())
                .serviceInfo(storeEntity.getServiceInfo())
                .reviewSpan(storeEntity.getReviewSpan())
                .heartStore(heartStore)
                .useInfo(storeEntity.getUseInfo())
                .saleInfo(storeEntity.getSaleInfo())
                .placeInfo(storeEntity.getPlaceInfo())
                .address(storeEntity.getAddress())
                .sponInfo(storeEntity.getSponInfo())
                .reviewCount(storeEntity.getReviewCount())
                .name(storeEntity.getName())
                .submitReview(submitReview).build();
    }


    /**
     * Store와 CouponRepository를 받아 storeDto와 couponDto를 반환하는 기능
     */
    public static MyPageStoreResponseDto convertToMyStoreDto(Long memberId, StoreEntity storeEntity, CouponRepository couponRepository) {
        if (storeEntity == null) {
            return null;
        }

        MyPageStoreResponseDto storeResponseDto = MyPageStoreResponseDto.builder()
                .storeId(storeEntity.getId())
                .name(storeEntity.getName())
                .title(storeEntity.getTitle())
                .image(storeEntity.getImage())
                .build();

        // Coupon 정보 설정
        /*
        Optional<Coupon> optionalCoupon = couponRepository.findByMemberIdAndStoreId(memberId, store.getId());

        if (optionalCoupon.isPresent()) {
            MyPageCouponResponseDTO couponResponseDTO = CouponConverter.convertToMyCouponDto(optionalCoupon.get());
            storeResponseDto.setCouponDto(couponResponseDTO);
        }
         */

        List<CouponEntity> optionalCouponEntity = couponRepository.findAllByMemberIdAndStoreId(memberId, storeEntity.getId());

        if (!optionalCouponEntity.isEmpty()) {
            MyPageCouponResponseDTO couponResponseDTO = CouponConverter.convertToMyCouponDto(optionalCouponEntity.get(0));
            storeResponseDto.setCouponDto(couponResponseDTO);
        }

        return storeResponseDto;
    }
}