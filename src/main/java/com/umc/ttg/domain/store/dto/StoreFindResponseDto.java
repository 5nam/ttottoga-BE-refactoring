package com.umc.ttg.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreFindResponseDto {

    private String storeImage;
    private String title;
    private String name;
    private String subTitle;
    private String regionName;
    private String menuName;
    private String serviceInfo;
    private int reviewSpan;
    private boolean heartStore;
    private String useInfo;
    private String saleInfo;
    private String placeInfo;
    private String address;
    private String sponInfo;
    private int reviewCount;
    private boolean submitReview;

}
