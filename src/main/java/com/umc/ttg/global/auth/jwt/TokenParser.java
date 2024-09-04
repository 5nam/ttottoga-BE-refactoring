package com.umc.ttg.global.auth.jwt;

import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.global.common.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenParser {
    public String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        boolean hasAuthorization = StringUtils.hasText(authorization);
        if(!hasAuthorization) throw new MemberHandler(ResponseCode.ACCESS_TOKEN_NOT_FOUND);

        boolean isBearer = authorization.startsWith("Bearer ");
        if(!isBearer) throw new MemberHandler(ResponseCode.BEARER_PREFIX_VALUE_EXCEPTION);

        String token = authorization.substring(7);
        return token;
    }
}
