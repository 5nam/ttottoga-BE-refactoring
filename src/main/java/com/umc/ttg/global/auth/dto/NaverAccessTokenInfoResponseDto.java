package com.umc.ttg.global.auth.dto;

import lombok.Getter;

@Getter
public class NaverAccessTokenInfoResponseDto {
    private String resultcode;
    private String message;
    private ResponseData response;

    @Getter
    public static class ResponseData {
        private String id;
        private String profile_image;
        private String name;
    }

}

