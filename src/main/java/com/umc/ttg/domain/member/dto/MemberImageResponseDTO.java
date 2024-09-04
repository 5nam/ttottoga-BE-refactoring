package com.umc.ttg.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberImageResponseDTO {

    private Long memberId;
    private String profileImage;
}
