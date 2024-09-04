package com.umc.ttg.domain.member.application;

import com.umc.ttg.domain.member.dto.MyPageAllResponseDto;
import com.umc.ttg.global.common.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberQueryService {
    BaseResponseDto<MyPageAllResponseDto> myPageLookUp(String memberName);
}
