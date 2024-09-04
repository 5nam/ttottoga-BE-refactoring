package com.umc.ttg.domain.member.dto.converter;

import com.umc.ttg.domain.member.dto.MyPageMemberResponseDTO;
import com.umc.ttg.domain.member.entity.Member;

public class MemberConverter {

    public static MyPageMemberResponseDTO convertToMyMemberDto(Member member){

        return MyPageMemberResponseDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .benefitCount(member.getBenefitCount())
                .profileImage(member.getProfileImage())
                .build();
    }
}
