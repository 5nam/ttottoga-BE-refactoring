package com.umc.ttg.domain.member.api;

import com.umc.ttg.domain.member.application.MemberCommandService;
import com.umc.ttg.domain.member.application.MemberQueryService;
import com.umc.ttg.domain.member.dto.MemberImageRequestDTO;
import com.umc.ttg.domain.member.dto.MemberImageResponseDTO;
import com.umc.ttg.domain.member.dto.MyPageAllResponseDto;
import com.umc.ttg.global.common.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
//    private final AuthService authService;

    @GetMapping("/profile")
    public BaseResponseDto<MyPageAllResponseDto> getMyPage(HttpServletRequest request) {

        /**
         * FIXME: 로그인 구현 후 수정
         */

//        String memberName = authService.retrieveName(request);
        String memberName = "";

        return memberQueryService.myPageLookUp(memberName);
    }

    @PostMapping("/profile/image")
    public BaseResponseDto<MemberImageResponseDTO> modifyProfileImage
            (@ModelAttribute @Valid MemberImageRequestDTO memberImageRequestDTO,
             HttpServletRequest request) throws IOException {

        /**
         * FIXME: 로그인 구현 후 수정
         */

//        String memberName = authService.retrieveName(request);
        String memberName = "";

        return memberCommandService.updateImage(memberImageRequestDTO, memberName);
    }
}
