package com.umc.ttg.domain.member.exception.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(ResponseCode errorCode) {

        super(errorCode);
    }
}
