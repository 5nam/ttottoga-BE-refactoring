package com.umc.ttg.domain.store.dto;

import com.umc.ttg.domain.review.entity.ReviewEntity;
import com.umc.ttg.domain.store.dto.converter.StoreConverter;
import com.umc.ttg.domain.store.entity.StoreEntity;
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

        public Top15(StoreEntity storeEntity, Boolean isHeartStore) {
            this.storeId = storeEntity.getId();
            this.storeTitle = storeEntity.getTitle();
            this.storeImage = storeEntity.getImage();
            this.reviewCount = storeEntity.getReviewCount();
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

        public HotStore(StoreEntity storeEntity) {
            this.storeId = storeEntity.getId();
            this.storeTitle = storeEntity.getTitle();
            this.storeImage = storeEntity.getImage();
            this.serviceInfo = storeEntity.getServiceInfo();
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

        public HomeReviews(ReviewEntity reviewEntity) {
            this.reviewId = reviewEntity.getId();
            this.reviewImage = StoreConverter.convertToReviewImage(reviewEntity);
            this.profileImage = StoreConverter.convertToReviewProfileImage(reviewEntity);
            this.nickname = StoreConverter.convertToReviewNickname(reviewEntity);
            this.reviewTitle = StoreConverter.convertToReviewTitle(reviewEntity);
            this.storeName = reviewEntity.getStore().getName();
            this.reviewLink = reviewEntity.getReviewLink();
        }
    }

}
