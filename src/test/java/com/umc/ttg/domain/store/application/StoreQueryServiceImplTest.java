package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.StoreFindResponseDto;
import com.umc.ttg.domain.store.dto.StoreResultResponseDto;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.global.common.BaseResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql("/sql/store-query-service-test-data.sql")
@ActiveProfiles("test")
class StoreQueryServiceImplTest {

    @Autowired
    StoreQueryService storeQueryService;

    @Test
    void store_id와_memberName_으로_스토어를_조회할_수_있다() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId";
        // when
        BaseResponseDto<StoreFindResponseDto> response = storeQueryService.getById(storeId, memberName);
        // then
        assertThat(response.getResult().getName()).isEqualTo("testStore");
    }

    @Test
    void 잘못된_store_id로_스토어_조회하면_예외를_발생시킨다() {
        // given
        Long storeId = 3L;
        String memberName = "Kakao_userId";
        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getById(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("존재하지 않는 상점입니다.");
    }

    @Test
    void 잘못된_memberName으로_스토어_조회하면_예외를_발생시킨다() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId11111";
        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getById(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    void memberName이_null일_때도_스토어_조회할_수_있다() {
        // given
        Long storeId = 1L;
        String memberName = null;
        // when
        BaseResponseDto<StoreFindResponseDto> response = storeQueryService.getById(storeId, memberName);
        // then
        assertThat(response.getResult().getName()).isEqualTo("testStore");
    }

    @Test
    void regionId로_상점_게시글들을_한번에_20개씩_불러올_수_있다() {
        // given
        Long regionId = 1L;
        String memberName = null;
        int size = 0;
        int page = 0;

        // when
        BaseResponseDto<Page<StoreResultResponseDto>> response = storeQueryService.getByRegion(regionId, size, page, memberName);

        // then

    }

    @Test
    void getByIdByMenu() {
    }

    @Test
    void search() {
    }

    @Test
    void findHeartStores() {
    }

    @Test
    void findHome() {
    }
}