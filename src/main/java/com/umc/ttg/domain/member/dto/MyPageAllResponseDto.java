package com.umc.ttg.domain.member.dto;

import com.umc.ttg.domain.review.dto.MyPageReviewResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageAllResponseDto {

    private MyPageMemberResponseDTO member;
    private List<MyPageReviewResponseDTO> reviewDtos;
}
