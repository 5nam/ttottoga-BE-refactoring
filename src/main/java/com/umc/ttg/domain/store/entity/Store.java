package com.umc.ttg.domain.store.entity;

import com.umc.ttg.domain.store.dto.StoreRequestDto;
import com.umc.ttg.global.util.Time;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Getter
@Entity
public class Store extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 100)
    private String subTitle;

    private String image;

    private String useInfo;

    private String saleInfo;

    private String placeInfo;

    private String sponInfo;

    private String serviceInfo;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'n'")
    private Character hotYn;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private int reviewSpan;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Builder
    private Store(StoreRequestDto storeRequestDto, Menu menu, Region region, String storeImage) {

        this.title = storeRequestDto.getTitle();
        this.subTitle = storeRequestDto.getSubTitle();
        this.useInfo = storeRequestDto.getUseInfo();
        this.saleInfo = storeRequestDto.getSaleInfo();
        this.placeInfo = storeRequestDto.getPlaceInfo();
        this.sponInfo = storeRequestDto.getSponInfo();
        this.serviceInfo = storeRequestDto.getServiceInfo();
        this.reviewSpan = storeRequestDto.getReviewSpan();
        this.address = storeRequestDto.getAddress();
        this.name = storeRequestDto.getName();
        this.menu = menu;
        this.region = region;
        this.image = storeImage;

    }

    public void update(StoreRequestDto storeRequestDto, Menu menu, Region region, String storeImage) {
        this.title = storeRequestDto.getTitle();
        this.subTitle = storeRequestDto.getSubTitle();
        this.useInfo = storeRequestDto.getUseInfo();
        this.saleInfo = storeRequestDto.getSaleInfo();
        this.placeInfo = storeRequestDto.getPlaceInfo();
        this.sponInfo = storeRequestDto.getSponInfo();
        this.serviceInfo = storeRequestDto.getServiceInfo();
        this.reviewSpan = storeRequestDto.getReviewSpan();
        this.address = storeRequestDto.getAddress();
        this.name = storeRequestDto.getName();
        this.menu = menu;
        this.region = region;
        this.image = storeImage;
    }

    public void updateReviewCount() {
        this.reviewCount += 1;
    }

}
