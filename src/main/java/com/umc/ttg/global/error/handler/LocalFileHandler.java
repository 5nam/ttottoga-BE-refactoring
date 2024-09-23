package com.umc.ttg.global.error.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class LocalFileHandler extends GeneralException {
    public LocalFileHandler(ResponseCode responseCode) {
        super(responseCode);
    }
}
