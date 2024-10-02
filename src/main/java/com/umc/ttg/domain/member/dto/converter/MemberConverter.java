package com.umc.ttg.domain.member.dto.converter;

import com.umc.ttg.domain.member.dto.MyPageMemberResponseDTO;
import com.umc.ttg.domain.member.entity.MemberEntity;

public class MemberConverter {

    public static MyPageMemberResponseDTO convertToMyMemberDto(MemberEntity memberEntity){

        return MyPageMemberResponseDTO.builder()
                .memberId(memberEntity.getId())
                .nickname(memberEntity.getNickname())
                .benefitCount(memberEntity.getBenefitCount())
                .profileImage(memberEntity.getProfileImage())
                .build();
    }
}
