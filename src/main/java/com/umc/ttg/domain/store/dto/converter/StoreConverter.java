package com.umc.ttg.domain.store.dto.converter;

import com.umc.ttg.domain.coupon.dto.MyPageCouponResponseDTO;
import com.umc.ttg.domain.coupon.dto.converter.CouponConverter;
import com.umc.ttg.domain.coupon.entity.Coupon;
import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.store.dto.MyPageStoreResponseDto;
import com.umc.ttg.domain.review.entity.Review;
import com.umc.ttg.domain.store.dto.StoreResponseDto;
import com.umc.ttg.domain.store.dto.StoreFindResponseDto;
import com.umc.ttg.domain.store.entity.Store;

import java.util.List;

public class StoreConverter {

    // Store 정보 -> StoreCreateResponseDto 로
    public static StoreResponseDto convertToStoreResponse(Long storeId) {

        return new StoreResponseDto(storeId);

    }

    // 리뷰 썸네일 -> 상점 이미지
    public static String convertToReviewImage(Review review) {

        return review.getStore().getImage();

    }

    // 리뷰 작성자 이미지 -> Member 프로필 이미지
    public static String convertToReviewProfileImage(Review review) {

        return review.getMember().getProfileImage();

    }

    // 리뷰 작성자 -> Member 닉네임
    public static String convertToReviewNickname(Review review) {

        return review.getMember().getNickname();

    }

    // 리뷰 타이틀 -> {Member 닉네임}'님의 또또가 리뷰'
    public static String convertToReviewTitle(Review review) {

        return review.getMember().getNickname() + " 님의 또또가 리뷰";

    }

    // Store 정보 -> StoreFindResponseDto 로
    public static StoreFindResponseDto convertToStoreFindResponseDto(Store store, boolean submitReview, boolean heartStore) {
        return StoreFindResponseDto.builder()
                .storeImage(store.getImage())
                .title(store.getTitle())
                .subTitle(store.getSubTitle())
                .regionName(store.getRegion().getName())
                .menuName(store.getMenu().getName())
                .serviceInfo(store.getServiceInfo())
                .reviewSpan(store.getReviewSpan())
                .heartStore(heartStore)
                .useInfo(store.getUseInfo())
                .saleInfo(store.getSaleInfo())
                .placeInfo(store.getPlaceInfo())
                .address(store.getAddress())
                .sponInfo(store.getSponInfo())
                .reviewCount(store.getReviewCount())
                .name(store.getName())
                .submitReview(submitReview).build();
    }


    /**
     * Store와 CouponRepository를 받아 storeDto와 couponDto를 반환하는 기능
     */
    public static MyPageStoreResponseDto convertToMyStoreDto(Long memberId, Store store, CouponRepository couponRepository) {
        if (store == null) {
            return null;
        }

        MyPageStoreResponseDto storeResponseDto = MyPageStoreResponseDto.builder()
                .storeId(store.getId())
                .name(store.getName())
                .title(store.getTitle())
                .image(store.getImage())
                .build();

        // Coupon 정보 설정
        /*
        Optional<Coupon> optionalCoupon = couponRepository.findByMemberIdAndStoreId(memberId, store.getId());

        if (optionalCoupon.isPresent()) {
            MyPageCouponResponseDTO couponResponseDTO = CouponConverter.convertToMyCouponDto(optionalCoupon.get());
            storeResponseDto.setCouponDto(couponResponseDTO);
        }
         */

        List<Coupon> optionalCoupon = couponRepository.findAllByMemberIdAndStoreId(memberId, store.getId());

        if (!optionalCoupon.isEmpty()) {
            MyPageCouponResponseDTO couponResponseDTO = CouponConverter.convertToMyCouponDto(optionalCoupon.get(0));
            storeResponseDto.setCouponDto(couponResponseDTO);
        }

        return storeResponseDto;
    }
}