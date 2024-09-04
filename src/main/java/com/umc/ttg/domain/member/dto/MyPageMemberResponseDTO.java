package com.umc.ttg.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageMemberResponseDTO {

    private Long memberId;
    private String nickname;
    private int benefitCount;
    private String profileImage;
}
