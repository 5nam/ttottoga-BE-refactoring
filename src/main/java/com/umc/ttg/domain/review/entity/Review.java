package com.umc.ttg.domain.review.entity;

import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.review.dto.ReviewRegisterRequestDTO;
import com.umc.ttg.domain.store.entity.Store;
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
public class Review extends Time {

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
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Review(Store storeId, Member memberId, ReviewRegisterRequestDTO reviewRegisterRequestDTO) {

        this.store = storeId;
        this.member = memberId;
        this.reviewLink = reviewRegisterRequestDTO.getReviewLink();
    }
}
