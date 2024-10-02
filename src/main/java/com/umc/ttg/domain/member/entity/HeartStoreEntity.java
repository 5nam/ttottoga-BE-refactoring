package com.umc.ttg.domain.member.entity;

import com.umc.ttg.domain.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Getter @Setter
@Entity @Builder
public class HeartStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

}
