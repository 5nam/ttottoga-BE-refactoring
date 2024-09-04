package com.umc.ttg.domain.member.entity;

import com.umc.ttg.global.util.Time;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // Kakao_userId || Naver_userId 가 저장됨
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String nickname;

    private String profileImage;

    @Column(length = 15)
    private String phoneNum;

    @Column(nullable = false)
    private int benefitCount;

//     For Spring Security
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String role;

    @Builder
    public Member(String name, String nickname, String profileImage, String phoneNum, int benefitCount) {
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.phoneNum = phoneNum;
        this.benefitCount = benefitCount;
    }

    public Member (String userId, String nickname, String profileImage, String type) {
        this.name = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.phoneNum = null;
        this.benefitCount = 0;
        this.password = "Passw0rd";
        this.type = type;
        this.role = "ROLE_USER";
    }

    public void updateBenefitCount() {
        this.benefitCount += 1;
    }

}
