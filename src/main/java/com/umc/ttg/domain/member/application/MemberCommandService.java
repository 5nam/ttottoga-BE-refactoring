package com.umc.ttg.domain.member.application;

import com.umc.ttg.domain.member.dto.MemberImageRequestDTO;
import com.umc.ttg.domain.member.dto.MemberImageResponseDTO;
import com.umc.ttg.global.common.BaseResponseDto;

import java.io.IOException;

public interface MemberCommandService {

    BaseResponseDto<MemberImageResponseDTO> updateImage(MemberImageRequestDTO memberImageRequestDTO, String memberName) throws IOException;
}
