package com.umc.ttg.domain.auth.exception.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class AuthHandler extends GeneralException {
    public AuthHandler(ResponseCode errorCode) {
        super(errorCode);
    }
}
