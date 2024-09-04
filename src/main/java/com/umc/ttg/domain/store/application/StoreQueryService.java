package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.global.common.BaseResponseDto;
import org.springframework.data.domain.Page;

public interface StoreQueryService {

    BaseResponseDto<StoreFindResponseDto> findStore(Long storeId, String memberName);

    BaseResponseDto<HomeResponseDto> getHome(String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> findStoreByRegion(Long regionId, int page, int size, String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> findStoreByMenu(Long menuId, int page, int size, String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> searchStore(String keyword, int page, int size, String memberName);

    BaseResponseDto<Page<StoreResultResponseDto>> getHeartStores(int page, int size, String memberName);

}
