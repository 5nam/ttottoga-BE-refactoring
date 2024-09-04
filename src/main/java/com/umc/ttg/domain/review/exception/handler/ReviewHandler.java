package com.umc.ttg.domain.review.exception.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class ReviewHandler extends GeneralException {

    public ReviewHandler(ResponseCode errorCode) {
        super(errorCode);
    }
}
