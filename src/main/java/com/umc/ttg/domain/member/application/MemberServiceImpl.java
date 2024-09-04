package com.umc.ttg.domain.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.ttg.domain.coupon.exception.handler.CouponHandler;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.global.auth.dto.KakaoAccessTokenInfoResponseDto;
import com.umc.ttg.global.auth.dto.NaverAccessTokenInfoResponseDto;
import com.umc.ttg.global.auth.jwt.TokenParser;
import com.umc.ttg.global.common.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final TokenParser tokenParser;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findMemberByName(String name) {
        return memberRepository.findByName(name).orElse(null);
    }

    public String retrieveName(HttpServletRequest request) {

        String hasPrefixAccessToken = tokenParser.parseBearerToken(request);

        String name = null;
        if (hasPrefixAccessToken.startsWith("kakao_")) {
            String accessToken = hasPrefixAccessToken.substring(6);
            name = "kakao_" + tokenPrefixKakao(accessToken);
        }
        else if (hasPrefixAccessToken.startsWith("naver_")) {
            String accessToken = hasPrefixAccessToken.substring(6);
            name = "naver_" + tokenPrefixNaver(accessToken);
        }
        else
            throw new MemberHandler(ResponseCode.TOKEN_PREFIX_VALUE_EXCEPTION);

        return name;
    }

    public String permitAllAccess(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        if(!StringUtils.hasText(authorization)) return null;

        return retrieveName(request);
    }

    public Long permitMemberOnly(HttpServletRequest request) {
        String name = request.getUserPrincipal().getName();
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new CouponHandler(ResponseCode.MEMBER_NOT_FOUND));
        return member.getId();
    }

    private String tokenPrefixNaver(String accessToken) {
        RestTemplate rt = new RestTemplate();

        // HttpHeaders 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // HttpHeader와 HttpBody를 하나의 객체로 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReq =
                new HttpEntity<>(null,headers);

        // Http 요청 - POST 방식, Response 변수의 응답
        ResponseEntity<String> resp = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                kakaoTokenReq,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        NaverAccessTokenInfoResponseDto accessTokenInfo = null;

        try {
            accessTokenInfo = objectMapper.readValue(resp.getBody(), NaverAccessTokenInfoResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info(String.valueOf(accessTokenInfo));
        log.info("Member Id : " + accessTokenInfo.getResponse().getId());

        return accessTokenInfo.getResponse().getId();
    }

    private String tokenPrefixKakao(String accessToken) {
        RestTemplate rt = new RestTemplate();

        // HttpHeaders 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // HttpHeader와 HttpBody를 하나의 객체로 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReq =
                new HttpEntity<>(null,headers);

        // Http 요청 - POST 방식, Response 변수의 응답
        ResponseEntity<String> resp = rt.exchange(
                "https://kapi.kakao.com/v1/user/access_token_info",
                HttpMethod.GET,
                kakaoTokenReq,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoAccessTokenInfoResponseDto accessTokenInfo = null;

        try {
            accessTokenInfo = objectMapper.readValue(resp.getBody(), KakaoAccessTokenInfoResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Member Id : " + accessTokenInfo.getId());

        return accessTokenInfo.getId();
    }

}
