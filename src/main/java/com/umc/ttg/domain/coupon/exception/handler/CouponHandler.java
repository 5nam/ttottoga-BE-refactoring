package com.umc.ttg.domain.coupon.exception.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class CouponHandler extends GeneralException {
    public CouponHandler(ResponseCode errorCode) {
        super(errorCode);
    }
}
