package com.umc.ttg.global.auth.dto;

import lombok.Getter;

@Getter
public class KakaoAccessTokenInfoResponseDto {
    private String id;
    private Integer expires_in;
    private Integer app_id;
    private Integer expiresInMillis;
    private Integer appId;
}
