package com.umc.ttg.domain.store.exception.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class StoreHandler extends GeneralException {

    public StoreHandler(ResponseCode errorCode) {
        super(errorCode);
    }

}
