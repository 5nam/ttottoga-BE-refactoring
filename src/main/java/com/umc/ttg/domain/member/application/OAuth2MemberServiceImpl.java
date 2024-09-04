package com.umc.ttg.domain.member.application;

import com.umc.ttg.domain.member.entity.CustomOAuth2Member;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberServiceImpl extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String oauthClientName = request.getClientRegistration().getClientName();
        String accessToken = request.getAccessToken().getTokenValue();
        String refreshToken = null;

        log.info("accessToken : " + accessToken);

        Member member = null;
        String userId = null;
        String nickname = null;
        String profile_img = null;

        if(oauthClientName.equals("kakao")) {
            Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");

            userId = "kakao_" + oAuth2User.getAttributes().get("id");
            nickname = (String) properties.get("nickname");
            profile_img = (String) properties.get("profile_image");

            member = new Member(userId, nickname, profile_img, "kakao");
        }

        if (oauthClientName.equals("naver")) {
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");

            userId = "naver_" + responseMap.get("id");
            nickname = responseMap.get("name");
            profile_img = responseMap.get("profile_image");

            member = new Member(userId, nickname, profile_img, "naver");
        }

        // userId 확인 후 db 있는지 판별
            // 있으면, 데이터 update 후 로그인
            // 없으면, 회원가입 후 로그인


        if(memberService.findMemberByName(member.getName()) == null) {
            memberRepository.save(member);
        }
//        else {
//            memberService.update();
//        }

        if (refreshToken == null) return new CustomOAuth2Member(oauthClientName + "_" + accessToken, null);
        else return new CustomOAuth2Member(oauthClientName + "_" + accessToken, oauthClientName + "_" + refreshToken);
    }

}
