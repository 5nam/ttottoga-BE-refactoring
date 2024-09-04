package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.global.common.BaseResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface StoreCommandService {

    BaseResponseDto<StoreResponseDto> saveStore(StoreRequestDto storeRequestDto) throws IOException;

    BaseResponseDto<StoreResponseDto> updateStore(StoreRequestDto storeRequestDto, Long storeId) throws IOException;

    BaseResponseDto<HeartStoreResponseDto> insertHeart(Long storeId, String memberName);

    BaseResponseDto<HeartStoreResponseDto> deleteHeart(Long storeId, String memberName);

}
