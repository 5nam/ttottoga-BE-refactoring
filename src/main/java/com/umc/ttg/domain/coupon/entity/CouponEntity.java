package com.umc.ttg.domain.coupon.entity;

import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.global.util.Time;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@DynamicInsert @DynamicUpdate
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "COUPON")
public class CouponEntity extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String content;

    private String qrCode;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'N'")
    private Character statusYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Builder
    private CouponEntity(String name, String content, String qrCode, String imageUrl, LocalDate startDate, LocalDate endDate, Character statusYn, MemberEntity member, StoreEntity store) {
        this.name = name;
        this.content = content;
        this.qrCode = qrCode;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.statusYn = statusYn;
        this.member = member;
        this.store = store;
    }

    public static CouponEntity of(StoreEntity storeEntity, String qrCode, LocalDate startDate, LocalDate endDate, MemberEntity memberEntity) {
        return CouponEntity.builder()
                .name(storeEntity.getName())
                .content(storeEntity.getSubTitle())
                .imageUrl(storeEntity.getImage())
                .qrCode(qrCode)
                .startDate(startDate)
                .endDate(endDate)
                .statusYn('Y')
                .member(memberEntity)
                .store(storeEntity)
                .build();
    }

    public void updateStatus(Character statusYn) {
        this.statusYn = statusYn;
    }
}
