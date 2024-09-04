package com.umc.ttg.domain.store.dto;

import com.umc.ttg.domain.review.entity.Review;
import com.umc.ttg.domain.store.dto.converter.StoreConverter;
import com.umc.ttg.domain.store.entity.Store;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponseDto {

    private List<Top15> top15;
    private List<HotStore> hotStores;
    private List<HomeReviews> homeReviews;

    // 타입들
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class Top15 {
        Long storeId;
        String storeTitle;
        String storeImage;
        Integer reviewCount;
        Boolean isHeartStore;

        public Top15(Store store, Boolean isHeartStore) {
            this.storeId = store.getId();
            this.storeTitle = store.getTitle();
            this.storeImage = store.getImage();
            this.reviewCount = store.getReviewCount();
            this.isHeartStore = isHeartStore;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class HotStore {
        Long storeId;
        String storeTitle;
        String storeImage;
        String serviceInfo;

        public HotStore(Store store) {
            this.storeId = store.getId();
            this.storeTitle = store.getTitle();
            this.storeImage = store.getImage();
            this.serviceInfo = store.getServiceInfo();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class HomeReviews {
        Long reviewId;
        String reviewImage;
        String profileImage;
        String nickname;
        String reviewTitle;
        String storeName;
        String reviewLink;

        public HomeReviews(Review review) {
            this.reviewId = review.getId();
            this.reviewImage = StoreConverter.convertToReviewImage(review);
            this.profileImage = StoreConverter.convertToReviewProfileImage(review);
            this.nickname = StoreConverter.convertToReviewNickname(review);
            this.reviewTitle = StoreConverter.convertToReviewTitle(review);
            this.storeName = review.getStore().getName();
            this.reviewLink = review.getReviewLink();
        }
    }

}
