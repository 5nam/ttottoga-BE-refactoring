package com.umc.ttg.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.ttg.domain.member.entity.CustomOAuth2Member;
import com.umc.ttg.global.auth.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        String accessToken = oAuth2Member.getAccessToken();
        String refreshToken = oAuth2Member.getRefreshToken();

        // 헤더에 값 받아와지는지??
        response.addHeader("X-AUTH-TOKEN", accessToken);

        // 쿠키로 값 받아와지는지??
        ResponseCookie responseCookie = ResponseCookie.from("access_token", accessToken)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(60)
                .build();

//        Cookie cookie = new Cookie("access_token", accessToken);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(60);

        response.addHeader("Set-Cookie", responseCookie.toString());
//        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000/auth/oauth-response/" + accessToken);
    }

}
