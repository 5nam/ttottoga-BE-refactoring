package com.umc.ttg.global.error.handler;

import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.GeneralException;

public class AwsS3Handler extends GeneralException {

    public AwsS3Handler(ResponseCode errorCode) {
        super(errorCode);
    }

}
