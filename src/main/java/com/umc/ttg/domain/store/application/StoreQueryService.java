package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.global.common.BaseResponseDto;
import org.springframework.data.domain.Page;

public interface StoreQueryService {

    BaseResponseDto<StoreFindResponseDto> getById(Long storeId, String memberName);

    BaseResponseDto<HomeResponseDto> findHome(String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> getByRegion(Long regionId, int page, int size, String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> getByMenu(Long menuId, int page, int size, String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> search(String keyword, int page, int size, String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> findHeartStores(int page, int size, String memberName);

}
