package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.global.common.BaseResponseDto;

import java.io.IOException;

public interface StoreCommandService {

    BaseResponseDto<StoreResponseDto> create(StoreRequestDto storeRequestDto) throws IOException;

    BaseResponseDto<StoreResponseDto> update(StoreRequestDto storeRequestDto, Long storeId) throws IOException;

    BaseResponseDto<HeartStoreResponseDto> insertHeart(Long storeId, String memberName);

    BaseResponseDto<HeartStoreResponseDto> deleteHeart(Long storeId, String memberName);

}
