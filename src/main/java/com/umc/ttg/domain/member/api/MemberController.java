package com.umc.ttg.domain.member.api;

import com.umc.ttg.domain.member.application.MemberCommandService;
import com.umc.ttg.domain.member.application.MemberQueryService;
import com.umc.ttg.domain.member.application.MemberService;
import com.umc.ttg.domain.member.dto.MemberImageRequestDTO;
import com.umc.ttg.domain.member.dto.MemberImageResponseDTO;
import com.umc.ttg.domain.member.dto.MyPageAllResponseDto;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final MemberService memberService;

    @GetMapping("/profile")
    public BaseResponseDto<MyPageAllResponseDto> getMyPage(HttpServletRequest request) {

        String memberName = memberService.retrieveName(request);

        return memberQueryService.myPageLookUp(memberName);
    }

    @PostMapping("/profile/image")
    public BaseResponseDto<MemberImageResponseDTO> modifyProfileImage
            (@ModelAttribute @Valid MemberImageRequestDTO memberImageRequestDTO,
             HttpServletRequest request) throws IOException {

        String memberName = memberService.retrieveName(request);

        return memberCommandService.updateImage(memberImageRequestDTO, memberName);
    }

    @GetMapping
    public BaseResponseDto<Member> retrieveMember(HttpServletRequest request) {
        String name = memberService.retrieveName(request);
        Member member = memberService.findMemberByName(name);

        return BaseResponseDto.onSuccess(member, ResponseCode.OK);
    }

    // for server confirmation
    @GetMapping("/all")
    public List<Member> checkAuthorized() {
        return memberService.findAll();
    }

    // for server confirmation
    @GetMapping("/id")
    public BaseResponseDto retrieveAccessTokenInfo(HttpServletRequest request) {
        return BaseResponseDto.onSuccess(memberService.retrieveName(request), ResponseCode.OK);
    }
}
