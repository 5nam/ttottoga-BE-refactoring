package com.umc.ttg.domain.review.entity;

import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.review.dto.ReviewRegisterRequestDTO;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.global.util.Time;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Getter @Setter
@Entity
public class ReviewEntity extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String reviewLink;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(nullable = false)
    private LocalDate applyDate;

    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Builder
    public ReviewEntity(StoreEntity storeEntityId, MemberEntity memberEntityId, ReviewRegisterRequestDTO reviewRegisterRequestDTO) {

        this.store = storeEntityId;
        this.member = memberEntityId;
        this.reviewLink = reviewRegisterRequestDTO.getReviewLink();
    }
}
