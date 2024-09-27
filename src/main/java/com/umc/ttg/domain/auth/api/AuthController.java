package com.umc.ttg.domain.auth.api;

import com.umc.ttg.domain.auth.application.AuthService;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping
    public BaseResponseDto<Member> retrieveMember(HttpServletRequest request) {
        String name = authService.retrieveName(request);
        Member member = authService.findMemberByName(name);

        return BaseResponseDto.onSuccess(member, ResponseCode.OK);
    }

    // for server confirmation
    @GetMapping("/all")
    public List<Member> checkAuthorized() {
        return authService.findAll();
    }

    // for server confirmation
    @GetMapping("/id")
    public BaseResponseDto retrieveAccessTokenInfo(HttpServletRequest request) {
        return BaseResponseDto.onSuccess(authService.retrieveName(request), ResponseCode.OK);
    }
}
